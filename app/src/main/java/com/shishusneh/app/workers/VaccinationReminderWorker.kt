package com.shishusneh.app.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.shishusneh.app.R
import com.shishusneh.app.ui.dashboard.DashboardActivity
import java.util.*
import java.util.concurrent.TimeUnit

class VaccinationReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val vaccineId = inputData.getString("vaccine_id") ?: return Result.failure()
        val vaccineName = inputData.getString("vaccine_name") ?: "Vaccine"
        val isThreeDayReminder = inputData.getBoolean("is_3d", false)
        
        showNotification(vaccineId, vaccineName, isThreeDayReminder)
        return Result.success()
    }

    private fun showNotification(vaccineId: String, vaccineName: String, isThreeDayReminder: Boolean) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "vaccination_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Vaccination Reminders", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "vaccination")
            putExtra("vaccine_id", vaccineId)
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 
            vaccineId.hashCode() + (if (isThreeDayReminder) 1 else 0), 
            intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val title = if (isThreeDayReminder) "Upcoming Vaccination" else "Vaccination Due Today"
        val message = if (isThreeDayReminder) {
            "Reminder: $vaccineName is due in 3 days. Please prepare accordingly."
        } else {
            "Action Required: $vaccineName is due today. Visit your nearest health center."
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo_intern)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(applicationContext.getColor(R.color.accent_primary))
            .build()

        notificationManager.notify(vaccineId.hashCode() + (if (isThreeDayReminder) 1 else 0), notification)
    }

    companion object {
        fun schedule(context: Context, vaccineId: String, vaccineName: String, dueDate: Date) {
            val now = System.currentTimeMillis()
            
            // Schedule 3 days before
            val threeDaysBefore = dueDate.time - TimeUnit.DAYS.toMillis(3)
            if (threeDaysBefore > now) {
                enqueue(context, vaccineId, vaccineName, threeDaysBefore - now, "3d_$vaccineId", true)
            }

            // Schedule on the day (at 9 AM)
            val calendar = Calendar.getInstance().apply {
                time = dueDate
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            if (calendar.timeInMillis > now) {
                enqueue(context, vaccineId, vaccineName, calendar.timeInMillis - now, "day_$vaccineId", false)
            }
        }

        fun cancel(context: Context, vaccineId: String) {
            val workManager = WorkManager.getInstance(context)
            workManager.cancelAllWorkByTag("3d_$vaccineId")
            workManager.cancelAllWorkByTag("day_$vaccineId")
        }

        private fun enqueue(context: Context, id: String, name: String, delay: Long, tag: String, is3d: Boolean) {
            val data = Data.Builder()
                .putString("vaccine_id", id)
                .putString("vaccine_name", name)
                .putBoolean("is_3d", is3d)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<VaccinationReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(tag)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
