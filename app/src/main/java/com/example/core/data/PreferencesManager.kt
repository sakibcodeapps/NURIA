package com.example.core.data

import android.content.Context
import android.content.SharedPreferences
import com.example.core.model.CalculationMethod
import com.example.core.model.LastReadPosition
import com.example.core.model.LocationInfo
import com.example.core.model.Madhab
import com.example.core.prayer.PrayerCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("nuria_user_prefs", Context.MODE_PRIVATE)

    private val _locationFlow = MutableStateFlow(getLocation())
    val locationFlow: StateFlow<LocationInfo> = _locationFlow.asStateFlow()

    private val _bookmarksFlow = MutableStateFlow(getBookmarks())
    val bookmarksFlow: StateFlow<Set<String>> = _bookmarksFlow.asStateFlow()

    private val _favoriteDuasFlow = MutableStateFlow(getFavoriteDuas())
    val favoriteDuasFlow: StateFlow<Set<String>> = _favoriteDuasFlow.asStateFlow()

    // Location
    fun getLocation(): LocationInfo {
        val lat = prefs.getFloat(KEY_LAT, 23.8103f).toDouble()
        val lng = prefs.getFloat(KEY_LNG, 90.4125f).toDouble()
        val city = prefs.getString(KEY_CITY, "Dhaka") ?: "Dhaka"
        val country = prefs.getString(KEY_COUNTRY, "Bangladesh") ?: "Bangladesh"
        val isGps = prefs.getBoolean(KEY_IS_GPS, false)
        return LocationInfo(latitude = lat, longitude = lng, cityName = city, countryName = country, isGps = isGps)
    }

    fun saveLocation(location: LocationInfo) {
        prefs.edit()
            .putFloat(KEY_LAT, location.latitude.toFloat())
            .putFloat(KEY_LNG, location.longitude.toFloat())
            .putString(KEY_CITY, location.cityName)
            .putString(KEY_COUNTRY, location.countryName)
            .putBoolean(KEY_IS_GPS, location.isGps)
            .apply()
        _locationFlow.value = location
    }

    // Prayer Calculation Settings
    fun getPrayerCalculationParams(): PrayerCalculator.PrayerCalculationParameters {
        val methodId = prefs.getString(KEY_CALC_METHOD, CalculationMethod.KARACHI.id)
        val method = CalculationMethod.entries.firstOrNull { it.id == methodId } ?: CalculationMethod.KARACHI

        val madhabId = prefs.getString(KEY_MADHAB, Madhab.HANAFI.id)
        val madhab = Madhab.entries.firstOrNull { it.id == madhabId } ?: Madhab.HANAFI

        return PrayerCalculator.PrayerCalculationParameters(
            method = method,
            madhab = madhab,
            fajrOffsetMinutes = prefs.getInt(KEY_OFFSET_FAJR, 0),
            sunriseOffsetMinutes = prefs.getInt(KEY_OFFSET_SUNRISE, 0),
            dhuhrOffsetMinutes = prefs.getInt(KEY_OFFSET_DHUHR, 0),
            asrOffsetMinutes = prefs.getInt(KEY_OFFSET_ASR, 0),
            maghribOffsetMinutes = prefs.getInt(KEY_OFFSET_MAGHRIB, 0),
            ishaOffsetMinutes = prefs.getInt(KEY_OFFSET_ISHA, 0)
        )
    }

    fun saveCalculationMethod(method: CalculationMethod) {
        prefs.edit().putString(KEY_CALC_METHOD, method.id).apply()
    }

    fun saveMadhab(madhab: Madhab) {
        prefs.edit().putString(KEY_MADHAB, madhab.id).apply()
    }

    // Prayer Notifications
    fun getPrayerNotificationSettings(): Map<String, Boolean> {
        val globalEnabled = prefs.getBoolean(KEY_NOTIF_ALL, true)
        if (!globalEnabled) {
            return mapOf(
                "fajr" to false, "sunrise" to false, "dhuhr" to false,
                "asr" to false, "maghrib" to false, "isha" to false
            )
        }
        return mapOf(
            "fajr" to prefs.getBoolean(KEY_NOTIF_FAJR, true),
            "sunrise" to prefs.getBoolean(KEY_NOTIF_SUNRISE, false),
            "dhuhr" to prefs.getBoolean(KEY_NOTIF_DHUHR, true),
            "asr" to prefs.getBoolean(KEY_NOTIF_ASR, true),
            "maghrib" to prefs.getBoolean(KEY_NOTIF_MAGHRIB, true),
            "isha" to prefs.getBoolean(KEY_NOTIF_ISHA, true)
        )
    }

    fun setAllNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIF_ALL, enabled).apply()
    }

    fun isAllNotificationsEnabled(): Boolean = prefs.getBoolean(KEY_NOTIF_ALL, true)

    fun setPrayerNotificationEnabled(prayerId: String, enabled: Boolean) {
        val key = when (prayerId) {
            "fajr" -> KEY_NOTIF_FAJR
            "sunrise" -> KEY_NOTIF_SUNRISE
            "dhuhr" -> KEY_NOTIF_DHUHR
            "asr" -> KEY_NOTIF_ASR
            "maghrib" -> KEY_NOTIF_MAGHRIB
            "isha" -> KEY_NOTIF_ISHA
            else -> return
        }
        prefs.edit().putBoolean(key, enabled).apply()
    }

    // Quran Preferences
    fun getQuranFontSize(): Float = prefs.getFloat(KEY_QURAN_FONT_SIZE, 24f)

    fun saveQuranFontSize(size: Float) {
        prefs.edit().putFloat(KEY_QURAN_FONT_SIZE, size).apply()
    }

    fun isShowBanglaTranslation(): Boolean = prefs.getBoolean(KEY_SHOW_TRANSLATION_BN, true)
    fun setShowBanglaTranslation(show: Boolean) = prefs.edit().putBoolean(KEY_SHOW_TRANSLATION_BN, show).apply()

    fun isShowEnglishTranslation(): Boolean = prefs.getBoolean(KEY_SHOW_TRANSLATION_EN, true)
    fun setShowEnglishTranslation(show: Boolean) = prefs.edit().putBoolean(KEY_SHOW_TRANSLATION_EN, show).apply()

    fun getLastReadPosition(): LastReadPosition {
        val surah = prefs.getInt(KEY_LAST_READ_SURAH, 1)
        val verse = prefs.getInt(KEY_LAST_READ_VERSE, 1)
        val name = prefs.getString(KEY_LAST_READ_NAME, "Al-Fatihah") ?: "Al-Fatihah"
        return LastReadPosition(surah, verse, name)
    }

    fun saveLastReadPosition(surah: Int, verse: Int, name: String) {
        prefs.edit()
            .putInt(KEY_LAST_READ_SURAH, surah)
            .putInt(KEY_LAST_READ_VERSE, verse)
            .putString(KEY_LAST_READ_NAME, name)
            .apply()
    }

    fun getBookmarks(): Set<String> {
        return prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
    }

    fun toggleBookmark(surahNumber: Int, verseNumber: Int): Boolean {
        val key = "$surahNumber:$verseNumber"
        val current = getBookmarks().toMutableSet()
        val newState: Boolean
        if (current.contains(key)) {
            current.remove(key)
            newState = false
        } else {
            current.add(key)
            newState = true
        }
        prefs.edit().putStringSet(KEY_BOOKMARKS, current).apply()
        _bookmarksFlow.value = current
        return newState
    }

    fun isVerseBookmarked(surahNumber: Int, verseNumber: Int): Boolean {
        return getBookmarks().contains("$surahNumber:$verseNumber")
    }

    // Duas Favorites
    fun getFavoriteDuas(): Set<String> {
        return prefs.getStringSet(KEY_FAV_DUAS, emptySet()) ?: emptySet()
    }

    fun toggleFavoriteDua(duaId: String): Boolean {
        val current = getFavoriteDuas().toMutableSet()
        val isFav: Boolean
        if (current.contains(duaId)) {
            current.remove(duaId)
            isFav = false
        } else {
            current.add(duaId)
            isFav = true
        }
        prefs.edit().putStringSet(KEY_FAV_DUAS, current).apply()
        _favoriteDuasFlow.value = current
        return isFav
    }

    // Tasbih
    fun getTasbihCount(): Int = prefs.getInt(KEY_TASBIH_COUNT, 0)
    fun saveTasbihCount(count: Int) = prefs.edit().putInt(KEY_TASBIH_COUNT, count).apply()

    fun getTasbihTarget(): Int = prefs.getInt(KEY_TASBIH_TARGET, 33)
    fun saveTasbihTarget(target: Int) = prefs.edit().putInt(KEY_TASBIH_TARGET, target).apply()

    fun isTasbihVibration(): Boolean = prefs.getBoolean(KEY_TASBIH_VIBRATE, true)
    fun setTasbihVibration(vibrate: Boolean) = prefs.edit().putBoolean(KEY_TASBIH_VIBRATE, vibrate).apply()

    fun isTasbihSound(): Boolean = prefs.getBoolean(KEY_TASBIH_SOUND, false)
    fun setTasbihSound(sound: Boolean) = prefs.edit().putBoolean(KEY_TASBIH_SOUND, sound).apply()

    // Onboarding
    fun isOnboardingCompleted(): Boolean = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
    fun setOnboardingCompleted(completed: Boolean) = prefs.edit().putBoolean(KEY_ONBOARDING_DONE, completed).apply()

    // Theme (0: System, 1: Light, 2: Dark)
    fun getThemeMode(): Int = prefs.getInt(KEY_THEME_MODE, 0)
    fun setThemeMode(mode: Int) = prefs.edit().putInt(KEY_THEME_MODE, mode).apply()

    companion object {
        private const val KEY_LAT = "loc_lat"
        private const val KEY_LNG = "loc_lng"
        private const val KEY_CITY = "loc_city"
        private const val KEY_COUNTRY = "loc_country"
        private const val KEY_IS_GPS = "loc_is_gps"

        private const val KEY_CALC_METHOD = "calc_method"
        private const val KEY_MADHAB = "calc_madhab"
        private const val KEY_OFFSET_FAJR = "offset_fajr"
        private const val KEY_OFFSET_SUNRISE = "offset_sunrise"
        private const val KEY_OFFSET_DHUHR = "offset_dhuhr"
        private const val KEY_OFFSET_ASR = "offset_asr"
        private const val KEY_OFFSET_MAGHRIB = "offset_maghrib"
        private const val KEY_OFFSET_ISHA = "offset_isha"

        private const val KEY_NOTIF_ALL = "notif_all"
        private const val KEY_NOTIF_FAJR = "notif_fajr"
        private const val KEY_NOTIF_SUNRISE = "notif_sunrise"
        private const val KEY_NOTIF_DHUHR = "notif_dhuhr"
        private const val KEY_NOTIF_ASR = "notif_asr"
        private const val KEY_NOTIF_MAGHRIB = "notif_maghrib"
        private const val KEY_NOTIF_ISHA = "notif_isha"

        private const val KEY_QURAN_FONT_SIZE = "quran_font_size"
        private const val KEY_SHOW_TRANSLATION_BN = "show_trans_bn"
        private const val KEY_SHOW_TRANSLATION_EN = "show_trans_en"
        private const val KEY_LAST_READ_SURAH = "last_read_surah"
        private const val KEY_LAST_READ_VERSE = "last_read_verse"
        private const val KEY_LAST_READ_NAME = "last_read_name"
        private const val KEY_BOOKMARKS = "quran_bookmarks"

        private const val KEY_FAV_DUAS = "favorite_duas"

        private const val KEY_TASBIH_COUNT = "tasbih_count"
        private const val KEY_TASBIH_TARGET = "tasbih_target"
        private const val KEY_TASBIH_VIBRATE = "tasbih_vibrate"
        private const val KEY_TASBIH_SOUND = "tasbih_sound"

        private const val KEY_ONBOARDING_DONE = "onboarding_done"
        private const val KEY_THEME_MODE = "app_theme_mode"

        // Preset Cities
        val PRESET_CITIES = listOf(
            LocationInfo(23.8103, 90.4125, "Dhaka", "Bangladesh"),
            LocationInfo(22.3569, 91.7832, "Chittagong", "Bangladesh"),
            LocationInfo(24.8949, 91.8687, "Sylhet", "Bangladesh"),
            LocationInfo(24.3745, 88.6042, "Rajshahi", "Bangladesh"),
            LocationInfo(22.8456, 89.5403, "Khulna", "Bangladesh"),
            LocationInfo(22.7010, 90.3535, "Barisal", "Bangladesh"),
            LocationInfo(25.7439, 89.2752, "Rangpur", "Bangladesh"),
            LocationInfo(24.7471, 90.4203, "Mymensingh", "Bangladesh"),
            LocationInfo(21.4225, 39.8262, "Makkah", "Saudi Arabia"),
            LocationInfo(24.4672, 39.6111, "Madinah", "Saudi Arabia"),
            LocationInfo(24.7136, 46.6753, "Riyadh", "Saudi Arabia"),
            LocationInfo(25.2048, 55.2708, "Dubai", "United Arab Emirates"),
            LocationInfo(30.0444, 31.2357, "Cairo", "Egypt"),
            LocationInfo(41.0082, 28.9784, "Istanbul", "Turkey"),
            LocationInfo(3.1390, 101.6869, "Kuala Lumpur", "Malaysia"),
            LocationInfo(-6.2088, 106.8456, "Jakarta", "Indonesia"),
            LocationInfo(24.8607, 67.0011, "Karachi", "Pakistan"),
            LocationInfo(31.5204, 74.3587, "Lahore", "Pakistan"),
            LocationInfo(28.6139, 77.2090, "New Delhi", "India"),
            LocationInfo(51.5074, -0.1278, "London", "United Kingdom"),
            LocationInfo(40.7128, -74.0060, "New York", "United States"),
            LocationInfo(43.6532, -79.3832, "Toronto", "Canada"),
            LocationInfo(-33.8688, 151.2093, "Sydney", "Australia")
        )
    }
}
