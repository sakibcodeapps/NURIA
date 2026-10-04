package com.example.core.localization

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.compositionLocalOf
import com.example.core.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class LanguageManager(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("nuria_language_prefs", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(loadInitialLanguage())
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private fun loadInitialLanguage(): AppLanguage {
        val savedCode = prefs.getString(KEY_LANGUAGE, null)
        if (savedCode != null) {
            return AppLanguage.fromCode(savedCode)
        }
        val defaultLocale = Locale.getDefault().language
        return when {
            defaultLocale.startsWith("bn") -> AppLanguage.BANGLA
            defaultLocale.startsWith("ar") -> AppLanguage.ARABIC
            defaultLocale.startsWith("en") -> AppLanguage.ENGLISH
            else -> AppLanguage.BANGLA // Default to Bangla as requested
        }
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
        _currentLanguage.value = language
        updateSystemLocale(language)
    }

    private fun updateSystemLocale(language: AppLanguage) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    companion object {
        private const val KEY_LANGUAGE = "selected_app_language"
        val LocalAppLanguage = compositionLocalOf { AppLanguage.BANGLA }
    }
}
