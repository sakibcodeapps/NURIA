package com.example.core.model

enum class Prayer(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val nameAr: String,
    val isMandatoryPrayer: Boolean = true
) {
    FAJR("fajr", "Fajr", "ফজর", "الفجر"),
    SUNRISE("sunrise", "Sunrise", "সূর্যোদয়", "الشروق", isMandatoryPrayer = false),
    DHUHR("dhuhr", "Dhuhr", "যোহর", "الظهر"),
    ASR("asr", "Asr", "আসর", "العصر"),
    MAGHRIB("maghrib", "Maghrib", "মাগরিব", "المغرب"),
    ISHA("isha", "Isha", "ইশা", "العشاء");

    fun getLocalizedName(language: AppLanguage): String {
        return when (language) {
            AppLanguage.BANGLA -> nameBn
            AppLanguage.ENGLISH -> nameEn
            AppLanguage.ARABIC -> nameAr
        }
    }
}

data class PrayerTimeItem(
    val prayer: Prayer,
    val timeMillis: Long,
    val formattedTime: String,
    val isNext: Boolean = false,
    val isPassed: Boolean = false,
    val notificationEnabled: Boolean = true
)

data class DailyPrayerSchedule(
    val dateString: String,
    val hijriDateString: String,
    val items: List<PrayerTimeItem>,
    val nextPrayer: PrayerTimeItem?,
    val remainingMillisToNext: Long,
    val countdownFormatted: String,
    val suhoorEndMillis: Long,
    val iftarMillis: Long,
    val formattedSuhoor: String,
    val formattedIftar: String
)

enum class CalculationMethod(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val nameAr: String,
    val fajrAngle: Double,
    val ishaAngle: Double,
    val ishaMinutesAfterMaghrib: Int? = null
) {
    KARACHI("karachi", "University of Islamic Sciences, Karachi", "ইসলামিক সায়েন্সেস বিশ্ববিদ্যালয়, করাচি", "جامعة العلوم الإسلامية بكراتشي", 18.0, 18.0),
    MWL("mwl", "Muslim World League", "মুসলিম ওয়ার্ল্ড লিগ", "رابطة العالم الإسلامي", 18.0, 17.0),
    ISNA("isna", "Islamic Society of North America (ISNA)", "ইসলামিক সোসাইটি অব নর্থ আমেরিকা", "الجمعية الإسلامية لأمريكا الشمالية", 15.0, 15.0),
    UMM_AL_QURA("umm_al_qura", "Umm al-Qura University, Makkah", "উম্মুল কুরা বিশ্ববিদ্যালয়, মক্কা", "جامعة أم القرى، مكة المكرمة", 18.5, 0.0, ishaMinutesAfterMaghrib = 90),
    EGYPTIAN("egyptian", "Egyptian General Authority of Survey", "মিশরীয় জরিপ কর্তৃপক্ষ", "الهيئة المصرية العامة للمساحة", 19.5, 17.5);

    fun getLocalizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> nameBn
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.ARABIC -> nameAr
    }
}

enum class Madhab(val id: String, val nameEn: String, val nameBn: String, val nameAr: String, val shadowMultiplier: Int) {
    SHAFI("shafi", "Standard (Shafi'i, Maliki, Hanbali)", "আদর্শ (শাফেয়ী, মালেকী, হাম্বলী)", "الشافعي والمالكي والحنبلي", 1),
    HANAFI("hanafi", "Hanafi", "হানাফী", "الحنفي", 2);

    fun getLocalizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.BANGLA -> nameBn
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.ARABIC -> nameAr
    }
}
