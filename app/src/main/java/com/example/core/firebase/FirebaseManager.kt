package com.example.core.firebase

import android.content.Context
import android.util.Log
import com.example.core.model.AdsConfig
import com.example.core.model.Announcement
import com.example.core.model.AppUpdateConfig
import com.example.core.model.RemoteBranding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseManager(private val context: Context) {

    private val _brandingFlow = MutableStateFlow(RemoteBranding())
    val brandingFlow: StateFlow<RemoteBranding> = _brandingFlow.asStateFlow()

    private val _announcementsFlow = MutableStateFlow<List<Announcement>>(emptyList())
    val announcementsFlow: StateFlow<List<Announcement>> = _announcementsFlow.asStateFlow()

    private val _updateConfigFlow = MutableStateFlow(AppUpdateConfig())
    val updateConfigFlow: StateFlow<AppUpdateConfig> = _updateConfigFlow.asStateFlow()

    private val _adsConfigFlow = MutableStateFlow(AdsConfig())
    val adsConfigFlow: StateFlow<AdsConfig> = _adsConfigFlow.asStateFlow()

    private var firestore: FirebaseFirestore? = null

    init {
        initFirebase()
    }

    private fun initFirebase() {
        try {
            val apps = FirebaseApp.getApps(context)
            if (apps.isNotEmpty()) {
                firestore = FirebaseFirestore.getInstance().apply {
                    firestoreSettings = FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build()
                }
            }
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Firebase initialization skipped or pending setup: ${e.message}")
        }
    }

    suspend fun fetchRemoteBranding(): RemoteBranding = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext _brandingFlow.value
        try {
            val snapshot = db.collection("branding").document("app").get().await()
            if (snapshot.exists()) {
                val branding = RemoteBranding(
                    appName = snapshot.getString("appName") ?: "NURIA",
                    tagline = snapshot.getString("tagline") ?: "Prayer • Quran • Qibla",
                    logoUrl = snapshot.getString("logoUrl"),
                    splashLogoUrl = snapshot.getString("splashLogoUrl"),
                    primaryColorHex = snapshot.getString("primaryColor") ?: "#006C4C",
                    secondaryColorHex = snapshot.getString("secondaryColor") ?: "#C5A059",
                    updatedAt = snapshot.getLong("updatedAt") ?: System.currentTimeMillis()
                )
                _brandingFlow.value = branding
                return@withContext branding
            }
        } catch (e: Exception) {
            Log.d("FirebaseManager", "Could not fetch remote branding, using fallback: ${e.message}")
        }
        return@withContext _brandingFlow.value
    }

    suspend fun fetchAnnouncements(): List<Announcement> = withContext(Dispatchers.IO) {
        val fallback = listOf(
            Announcement(
                id = "welcome_announcement",
                titleBn = "নুরিয়া অ্যাপের শুভ উদ্বোধন",
                titleEn = "Welcome to NURIA Islamic Companion",
                titleAr = "مرحباً بكم في تطبيق نوريا",
                contentBn = "নামাজ, কুরআন ও কিবলার সঠিক নির্দেশনায় নুরিয়া আপনার সাথে। সকল ফিচার এখন অফলাইনেও উপভোগ করুন।",
                contentEn = "Your complete guide for prayers, Quran recitations, and Qibla direction. Available offline and online.",
                contentAr = "دليلك الشامل لمواقيت الصلاة والقرآن الكريم والقبلة مع دعم العمل بدون إنترنت.",
                date = "2026",
                isImportant = true,
                isPublished = true
            ),
            Announcement(
                id = "ramadan_prep",
                titleBn = "পবিত্র রমজানের প্রস্তুতি",
                titleEn = "Preparing for Blessed Ramadan",
                titleAr = "الاستعداد لشهر رمضان المبارك",
                contentBn = "সিয়াম সাধনার প্রস্তুতি গ্রহণ করুন। সেহরি ও ইফতারের সঠিক সময় পেতে রোজা ট্যাব দেখুন।",
                contentEn = "Prepare spiritually for the holy month. Check the Fasting tab for precise Suhoor and Iftar timings.",
                contentAr = "استعد روحانياً للشهر الفضيل. تفقد قسم الصيام للاطلاع على مواقيت السحور والإفطار.",
                date = "2026",
                isImportant = false,
                isPublished = true
            )
        )

        val db = firestore ?: run {
            _announcementsFlow.value = fallback
            return@withContext fallback
        }

        try {
            val snapshot = db.collection("announcements")
                .whereEqualTo("isPublished", true)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val list = snapshot.documents.mapNotNull { doc ->
                    try {
                        Announcement(
                            id = doc.id,
                            titleBn = doc.getString("title_bn") ?: doc.getString("titleBn") ?: "",
                            titleEn = doc.getString("title_en") ?: doc.getString("titleEn") ?: "",
                            titleAr = doc.getString("title_ar") ?: doc.getString("titleAr") ?: "",
                            contentBn = doc.getString("content_bn") ?: doc.getString("contentBn") ?: "",
                            contentEn = doc.getString("content_en") ?: doc.getString("contentEn") ?: "",
                            contentAr = doc.getString("content_ar") ?: doc.getString("contentAr") ?: "",
                            date = doc.getString("date") ?: "",
                            isImportant = doc.getBoolean("isImportant") ?: false,
                            isPublished = doc.getBoolean("isPublished") ?: true
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                if (list.isNotEmpty()) {
                    _announcementsFlow.value = list
                    return@withContext list
                }
            }
        } catch (e: Exception) {
            Log.d("FirebaseManager", "Could not fetch announcements from Firestore: ${e.message}")
        }

        _announcementsFlow.value = fallback
        return@withContext fallback
    }

    suspend fun checkAppUpdateConfig(): AppUpdateConfig = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext _updateConfigFlow.value
        try {
            val doc = db.collection("app_config").document("version").get().await()
            if (doc.exists()) {
                val config = AppUpdateConfig(
                    minSupportedVersion = doc.getLong("minSupportedVersion")?.toInt() ?: 1,
                    latestVersion = doc.getLong("latestVersion")?.toInt() ?: 1,
                    isMaintenanceMode = doc.getBoolean("isMaintenanceMode") ?: false,
                    forceUpdate = doc.getBoolean("forceUpdate") ?: false,
                    messageBn = doc.getString("message_bn") ?: doc.getString("messageBn") ?: _updateConfigFlow.value.messageBn,
                    messageEn = doc.getString("message_en") ?: doc.getString("messageEn") ?: _updateConfigFlow.value.messageEn,
                    messageAr = doc.getString("message_ar") ?: doc.getString("messageAr") ?: _updateConfigFlow.value.messageAr
                )
                _updateConfigFlow.value = config
                return@withContext config
            }
        } catch (e: Exception) {
            Log.d("FirebaseManager", "Could not fetch version config: ${e.message}")
        }
        return@withContext _updateConfigFlow.value
    }

    suspend fun fetchAdsConfig(): AdsConfig = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext _adsConfigFlow.value
        try {
            val doc = db.collection("app_config").document("ads").get().await()
            if (doc.exists()) {
                val config = AdsConfig(
                    isEnabled = doc.getBoolean("isEnabled") ?: true,
                    bannerAdUnitId = doc.getString("bannerAdUnitId") ?: _adsConfigFlow.value.bannerAdUnitId,
                    interstitialAdUnitId = doc.getString("interstitialAdUnitId") ?: _adsConfigFlow.value.interstitialAdUnitId,
                    showBannerOnHome = doc.getBoolean("showBannerOnHome") ?: true,
                    showBannerOnDua = doc.getBoolean("showBannerOnDua") ?: true,
                    showBannerOnAzkar = doc.getBoolean("showBannerOnAzkar") ?: true
                )
                _adsConfigFlow.value = config
                return@withContext config
            }
        } catch (e: Exception) {
            Log.d("FirebaseManager", "Could not fetch ads config: ${e.message}")
        }
        return@withContext _adsConfigFlow.value
    }
}
