package com.example

import android.app.Application
import com.example.core.ads.AdsService
import com.example.core.data.PreferencesManager
import com.example.core.firebase.FirebaseManager
import com.example.core.localization.LanguageManager
import com.example.core.notification.PrayerAlarmScheduler
import com.example.core.notification.PrayerNotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NuriaApp : Application() {

    lateinit var languageManager: LanguageManager
        private set
    lateinit var preferencesManager: PreferencesManager
        private set
    lateinit var firebaseManager: FirebaseManager
        private set
    lateinit var alarmScheduler: PrayerAlarmScheduler
        private set
    lateinit var notificationHelper: PrayerNotificationHelper
        private set
    lateinit var adsService: AdsService
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        languageManager = LanguageManager(this)
        preferencesManager = PreferencesManager(this)
        firebaseManager = FirebaseManager(this)
        alarmScheduler = PrayerAlarmScheduler(this)
        notificationHelper = PrayerNotificationHelper(this)
        adsService = AdsService(firebaseManager)

        // Initialize remote configs and branding safely in background
        appScope.launch {
            try {
                firebaseManager.fetchRemoteBranding()
                firebaseManager.fetchAnnouncements()
                firebaseManager.checkAppUpdateConfig()
                firebaseManager.fetchAdsConfig()
            } catch (_: Exception) {
                // Safe offline fallback in place
            }
        }
    }
}
