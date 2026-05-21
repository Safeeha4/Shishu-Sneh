package com.shishusneh.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shishusneh.app.data.local.dao.*
import com.shishusneh.app.data.local.entities.*

@Database(
    entities = [
        BabyProfile::class,
        GrowthEntry::class,
        VaccineRecord::class,
        MilestoneLog::class,
        ChatMessage::class,
        Consultation::class,
        Appointment::class,
        Doctor::class,
        FeedingSession::class
    ],
    version = 46, // Incremented version for FeedingSession entity
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShishuSnehDb : RoomDatabase() {
    
    abstract fun babyProfileDao(): BabyProfileDao
    abstract fun growthEntryDao(): GrowthEntryDao
    abstract fun vaccineRecordDao(): VaccineRecordDao
    abstract fun milestoneLogDao(): MilestoneLogDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun consultationDao(): ConsultationDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun doctorDao(): DoctorDao
    abstract fun feedingSessionDao(): FeedingSessionDao

    companion object {
        @Volatile
        private var INSTANCE: ShishuSnehDb? = null

        fun getDatabase(context: Context): ShishuSnehDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShishuSnehDb::class.java,
                    "shishu_sneh_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
