package com.example.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.core.model.DailyPrayerSchedule
import com.example.receiver.PrayerAlarmReceiver

class PrayerAlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun schedulePrayerAlarms(schedule: DailyPrayerSchedule) {
        if (alarmManager == null) return

        val now = System.currentTimeMillis()

        schedule.items.forEach { item ->
            if (item.notificationEnabled && item.prayer.isMandatoryPrayer && item.timeMillis > now) {
                scheduleAlarm(item.prayer.id, item.timeMillis, item.formattedTime)
            }
        }
    }

    private fun scheduleAlarm(prayerId: String, triggerMillis: Long, timeString: String) {
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_PRAYER_ALARM
            putExtra(EXTRA_PRAYER_ID, prayerId)
            putExtra(EXTRA_TIME_STRING, timeString)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prayerId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager?.canScheduleExactAlarms() == true) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                } else {
                    alarmManager?.setWindow(AlarmManager.RTC_WAKEUP, triggerMillis, 60000L, pendingIntent)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            } else {
                alarmManager?.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            }
        } catch (_: SecurityException) {
            // Handled when SCHEDULE_EXACT_ALARM is restricted
        }
    }

    fun cancelAllAlarms() {
        listOf("fajr", "dhuhr", "asr", "maghrib", "isha").forEach { id ->
            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                action = ACTION_PRAYER_ALARM
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager?.cancel(pendingIntent)
            }
        }
    }

    companion object {
        const val ACTION_PRAYER_ALARM = "com.example.ACTION_PRAYER_ALARM"
        const val EXTRA_PRAYER_ID = "extra_prayer_id"
        const val EXTRA_TIME_STRING = "extra_time_string"
    }
}
