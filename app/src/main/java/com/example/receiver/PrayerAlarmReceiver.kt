package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.NuriaApp
import com.example.core.notification.PrayerAlarmScheduler
import com.example.core.notification.PrayerNotificationHelper

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == PrayerAlarmScheduler.ACTION_PRAYER_ALARM) {
            val prayerId = intent.getStringExtra(PrayerAlarmScheduler.EXTRA_PRAYER_ID) ?: return
            val timeString = intent.getStringExtra(PrayerAlarmScheduler.EXTRA_TIME_STRING) ?: ""

            val app = context.applicationContext as? NuriaApp
            val lang = app?.languageManager?.currentLanguage?.value ?: com.example.core.model.AppLanguage.BANGLA
            val notificationHelper = PrayerNotificationHelper(context)
            notificationHelper.showPrayerNotification(prayerId, timeString, lang)
        }
    }
}
