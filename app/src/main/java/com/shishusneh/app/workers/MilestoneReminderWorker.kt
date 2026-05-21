package com.shishusneh.app.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.shishusneh.app.R
import com.shishusneh.app.ShishuSnehApplication
import java.util.*
import java.util.concurrent.TimeUnit

class MilestoneReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val application = applicationContext as ShishuSnehApplication
        val babyRepository = application.babyRepository
        val baby = babyRepository.getPrimaryProfile() ?: return Result.success()

        // Calculate age in weeks
        val diff = Date().time - baby.dateOfBirth.time
        val weeks = (diff / (1000 * 60 * 60 * 24 * 7)).toInt()

        showNotification(baby.name, weeks)
        return Result.success()
    }

    private fun showNotification(babyName: String, week: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "milestone_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Milestone Check-ins", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo_intern)
            .setContentTitle("Development Check-in")
            .setContentText("It's time for $babyName's Week $week milestones check!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setColor(applicationContext.getColor(R.color.accent_primary))
            .build()

        notificationManager.notify(2001, notification)
    }

    companion object {
        fun scheduleWeekly(context: Context, babyId: String) {
            val workRequest = PeriodicWorkRequestBuilder<MilestoneReminderWorker>(7, TimeUnit.DAYS)
                .addTag("milestone_reminder_$babyId")
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "milestone_reminder_$babyId",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}
