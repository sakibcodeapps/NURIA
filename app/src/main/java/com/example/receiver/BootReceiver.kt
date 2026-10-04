package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.NuriaApp
import java.util.Date

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == "android.intent.action.QUICKBOOT_POWERON") {
            val app = context.applicationContext as? NuriaApp ?: return
            val location = app.preferencesManager.getLocation()
            val params = app.preferencesManager.getPrayerCalculationParams()
            val enabledNotifs = app.preferencesManager.getPrayerNotificationSettings()

            val schedule = com.example.core.prayer.PrayerCalculator.calculateDailySchedule(
                date = Date(),
                location = location,
                params = params,
                enabledNotifications = enabledNotifs
            )
            app.alarmScheduler.schedulePrayerAlarms(schedule)
        }
    }
}
