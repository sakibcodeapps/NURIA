package com.example.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.Prayer

class PrayerNotificationHelper(private val context: Context) {

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val prayerChannel = NotificationChannel(
                CHANNEL_PRAYER,
                "Prayer Times & Adhan",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when prayer time arrives"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
            }

            val announcementChannel = NotificationChannel(
                CHANNEL_ANNOUNCEMENTS,
                "Announcements & Daily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Islamic reminders and app announcements"
            }

            notificationManager.createNotificationChannel(prayerChannel)
            notificationManager.createNotificationChannel(announcementChannel)
        }
    }

    fun showPrayerNotification(prayerId: String, timeString: String, language: AppLanguage) {
        val prayer = Prayer.entries.firstOrNull { it.id == prayerId } ?: Prayer.FAJR
        val prayerName = prayer.getLocalizedName(language)

        val title = when (language) {
            AppLanguage.BANGLA -> "ওয়াক্তের সময় হয়েছে: $prayerName"
            AppLanguage.ENGLISH -> "Prayer Time: $prayerName"
            AppLanguage.ARABIC -> "حان الآن موعد صلاة $prayerName"
        }

        val body = when (language) {
            AppLanguage.BANGLA -> "নামাজের জন্য প্রস্তুতি গ্রহণ করুন ($timeString)"
            AppLanguage.ENGLISH -> "Time to prepare for prayer ($timeString)"
            AppLanguage.ARABIC -> "حي على الصلاة ($timeString)"
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", "home")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            prayer.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_PRAYER)
            .setSmallIcon(R.drawable.nuria_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(prayer.ordinal + 100, notification)
        } catch (_: SecurityException) {
            // Handled when notification permission is not granted
        }
    }

    companion object {
        const val CHANNEL_PRAYER = "nuria_prayer_times"
        const val CHANNEL_ANNOUNCEMENTS = "nuria_announcements"
    }
}
