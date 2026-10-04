package com.example.core.model

data class LocationInfo(
    val latitude: Double = 23.8103, // Default to Dhaka
    val longitude: Double = 90.4125,
    val cityName: String = "Dhaka",
    val countryName: String = "Bangladesh",
    val isGps: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

data class QiblaData(
    val kaabaAngleDegrees: Float = 0f,
    val compassHeadingDegrees: Float = 0f,
    val relativeQiblaAngleDegrees: Float = 0f,
    val distanceToKaabaKm: Double = 0.0,
    val isSensorAvailable: Boolean = true,
    val accuracy: Int = 3,
    val needsCalibration: Boolean = false
)

data class QuranSurah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameBangla: String,
    val englishMeaning: String,
    val banglaMeaning: String,
    val totalVerses: Int,
    val revelationType: String // "Meccan" or "Medinan"
)

data class QuranVerse(
    val surahNumber: Int,
    val verseNumber: Int,
    val textArabic: String,
    val textBangla: String,
    val textEnglish: String,
    val isBookmarked: Boolean = false
)

data class Bookmark(
    val surahNumber: Int,
    val verseNumber: Int,
    val surahName: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class LastReadPosition(
    val surahNumber: Int = 1,
    val verseNumber: Int = 1,
    val surahName: String = "Al-Fatihah",
    val timestamp: Long = System.currentTimeMillis()
)

data class DuaCategory(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val nameAr: String,
    val icon: String
) {
    fun getLocalizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> nameBn
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.ARABIC -> nameAr
    }
}

data class DuaItem(
    val id: String,
    val categoryId: String,
    val titleBn: String,
    val titleEn: String,
    val titleAr: String,
    val contentAr: String,
    val contentBn: String,
    val contentEn: String,
    val reference: String,
    val isFavorite: Boolean = false
) {
    fun getLocalizedTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> titleBn
        AppLanguage.ENGLISH -> titleEn
        AppLanguage.ARABIC -> titleAr
    }

    fun getLocalizedContent(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> contentBn
        AppLanguage.ENGLISH -> contentEn
        AppLanguage.ARABIC -> contentAr
    }
}

data class AzkarItem(
    val id: String,
    val categoryId: String,
    val titleBn: String,
    val titleEn: String,
    val titleAr: String,
    val textAr: String,
    val textBn: String,
    val textEn: String,
    val benefitBn: String,
    val benefitEn: String,
    val benefitAr: String,
    val targetCount: Int = 33,
    val currentCount: Int = 0,
    val reference: String
) {
    fun getLocalizedTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> titleBn
        AppLanguage.ENGLISH -> titleEn
        AppLanguage.ARABIC -> titleAr
    }

    fun getLocalizedText(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> textBn
        AppLanguage.ENGLISH -> textEn
        AppLanguage.ARABIC -> textAr
    }

    fun getLocalizedBenefit(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> benefitBn
        AppLanguage.ENGLISH -> benefitEn
        AppLanguage.ARABIC -> benefitAr
    }
}

data class TasbihPreset(
    val id: String,
    val textAr: String,
    val nameBn: String,
    val nameEn: String,
    val target: Int
) {
    fun getLocalizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> nameBn
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.ARABIC -> textAr
    }
}

data class IslamicEvent(
    val id: String,
    val hijriDay: Int,
    val hijriMonth: Int,
    val titleBn: String,
    val titleEn: String,
    val titleAr: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val descriptionAr: String
) {
    fun getLocalizedTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> titleBn
        AppLanguage.ENGLISH -> titleEn
        AppLanguage.ARABIC -> titleAr
    }

    fun getLocalizedDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> descriptionBn
        AppLanguage.ENGLISH -> descriptionEn
        AppLanguage.ARABIC -> descriptionAr
    }
}

data class Announcement(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val titleAr: String,
    val contentBn: String,
    val contentEn: String,
    val contentAr: String,
    val date: String,
    val isImportant: Boolean = false,
    val isPublished: Boolean = true
) {
    fun getLocalizedTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> titleBn
        AppLanguage.ENGLISH -> titleEn
        AppLanguage.ARABIC -> titleAr
    }

    fun getLocalizedContent(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> contentBn
        AppLanguage.ENGLISH -> contentEn
        AppLanguage.ARABIC -> contentAr
    }
}

data class RemoteBranding(
    val appName: String = "NURIA",
    val tagline: String = "Prayer • Quran • Qibla",
    val logoUrl: String? = null,
    val splashLogoUrl: String? = null,
    val primaryColorHex: String = "#006C4C",
    val secondaryColorHex: String = "#C5A059",
    val updatedAt: Long = System.currentTimeMillis()
)

data class AppUpdateConfig(
    val minSupportedVersion: Int = 1,
    val latestVersion: Int = 1,
    val isMaintenanceMode: Boolean = false,
    val forceUpdate: Boolean = false,
    val messageBn: String = "অ্যাপ রক্ষণাবেক্ষণ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।",
    val messageEn: String = "App is currently under scheduled maintenance. Please check back shortly.",
    val messageAr: String = "التطبيق قيد الصيانة المجدولة حالياً. يرجى التحقق مرة أخرى قريباً."
) {
    fun getLocalizedMessage(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> messageBn
        AppLanguage.ENGLISH -> messageEn
        AppLanguage.ARABIC -> messageAr
    }
}

data class AdsConfig(
    val isEnabled: Boolean = true,
    val bannerAdUnitId: String = "ca-app-pub-3940256099942544/6300978111", // Standard AdMob test ID
    val interstitialAdUnitId: String = "ca-app-pub-3940256099942544/1033173712",
    val showBannerOnHome: Boolean = true,
    val showBannerOnDua: Boolean = true,
    val showBannerOnAzkar: Boolean = true
)
