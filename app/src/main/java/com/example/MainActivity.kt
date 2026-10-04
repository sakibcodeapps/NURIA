package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.core.calendar.HijriCalendarHelper
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.DailyPrayerSchedule
import com.example.core.model.LocationInfo
import com.example.core.prayer.PrayerCalculator
import com.example.ui.components.NuriaTopBar
import com.example.ui.screens.azkar.AzkarScreen
import com.example.ui.screens.calendar.CalendarScreen
import com.example.ui.screens.dua.DuaScreen
import com.example.ui.screens.fasting.FastingScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.maintenance.MaintenanceScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.qibla.QiblaScreen
import com.example.ui.screens.quran.QuranScreen
import com.example.ui.screens.quran.SurahDetailScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.tasbih.TasbihScreen
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.NuriaTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as NuriaApp
        val languageManager = app.languageManager
        val preferencesManager = app.preferencesManager
        val firebaseManager = app.firebaseManager
        val alarmScheduler = app.alarmScheduler
        val adsService = app.adsService

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            val currentLanguage by languageManager.currentLanguage.collectAsState()
            val remoteBranding by firebaseManager.brandingFlow.collectAsState()
            val announcements by firebaseManager.announcementsFlow.collectAsState()
            val updateConfig by firebaseManager.updateConfigFlow.collectAsState()
            val adsConfig by adsService.adsConfig.collectAsState()

            val location by preferencesManager.locationFlow.collectAsState()

            var isOnboardingDone by remember {
                mutableStateOf(preferencesManager.isOnboardingCompleted())
            }

            var currentScreen by remember { mutableStateOf("home") }
            var selectedSurahNumber by remember { mutableIntStateOf(1) }

            // Prayer schedule state
            var prayerSchedule by remember {
                mutableStateOf(
                    PrayerCalculator.calculateDailySchedule(
                        date = Date(),
                        location = location,
                        params = preferencesManager.getPrayerCalculationParams(),
                        enabledNotifications = preferencesManager.getPrayerNotificationSettings()
                    )
                )
            }

            // Function to recalculate schedule
            fun refreshSchedule() {
                val newSchedule = PrayerCalculator.calculateDailySchedule(
                    date = Date(),
                    location = location,
                    params = preferencesManager.getPrayerCalculationParams(),
                    enabledNotifications = preferencesManager.getPrayerNotificationSettings()
                )
                prayerSchedule = newSchedule
                alarmScheduler.schedulePrayerAlarms(newSchedule)
            }

            // Try to acquire GPS location if permission granted
            LaunchedEffect(Unit) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                            if (loc != null) {
                                val updated = location.copy(
                                    latitude = loc.latitude,
                                    longitude = loc.longitude,
                                    isGps = true
                                )
                                preferencesManager.saveLocation(updated)
                                refreshSchedule()
                            }
                        }
                    } catch (_: SecurityException) {}
                }
            }

            // Recalculate whenever location or date changes
            LaunchedEffect(location) {
                refreshSchedule()
            }

            val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                NuriaTheme {
                    if (updateConfig.isMaintenanceMode) {
                        MaintenanceScreen(
                            updateConfig = updateConfig,
                            language = currentLanguage,
                            onRetry = {
                                (application as NuriaApp).let {
                                    // re-check maintenance mode
                                }
                            }
                        )
                    } else if (!isOnboardingDone) {
                        OnboardingScreen(
                            language = currentLanguage,
                            onComplete = {
                                preferencesManager.setOnboardingCompleted(true)
                                isOnboardingDone = true
                            }
                        )
                    } else {
                        MainAppContent(
                            currentScreen = currentScreen,
                            selectedSurahNumber = selectedSurahNumber,
                            currentLanguage = currentLanguage,
                            remoteBranding = remoteBranding,
                            announcements = announcements,
                            location = location,
                            schedule = prayerSchedule,
                            preferencesManager = preferencesManager,
                            showAds = adsConfig.isEnabled,
                            onNavigate = { route -> currentScreen = route },
                            onOpenSurah = { sNum ->
                                selectedSurahNumber = sNum
                                currentScreen = "surah_detail"
                            },
                            onLanguageChange = { lang ->
                                languageManager.setLanguage(lang)
                                refreshSchedule()
                            },
                            onToggleNotification = { prayerId, enabled ->
                                preferencesManager.setPrayerNotificationEnabled(prayerId, enabled)
                                refreshSchedule()
                            },
                            onLocationClick = { currentScreen = "settings" },
                            onPrayerParamsChange = { refreshSchedule() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppContent(
    currentScreen: String,
    selectedSurahNumber: Int,
    currentLanguage: AppLanguage,
    remoteBranding: com.example.core.model.RemoteBranding,
    announcements: List<com.example.core.model.Announcement>,
    location: LocationInfo,
    schedule: DailyPrayerSchedule,
    preferencesManager: com.example.core.data.PreferencesManager,
    showAds: Boolean,
    onNavigate: (String) -> Unit,
    onOpenSurah: (Int) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onToggleNotification: (String, Boolean) -> Unit,
    onLocationClick: () -> Unit,
    onPrayerParamsChange: () -> Unit
) {
    // BackHandler for secondary screens
    if (currentScreen !in listOf("home", "quran", "qibla", "dua", "settings")) {
        BackHandler {
            onNavigate("home")
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen != "surah_detail") {
                NuriaTopBar(
                    appName = remoteBranding.appName,
                    location = location,
                    hijriDate = schedule.hijriDateString,
                    currentLanguage = currentLanguage,
                    onLanguageChange = onLanguageChange,
                    onLocationClick = onLocationClick
                )
            }
        },
        bottomBar = {
            if (currentScreen != "surah_detail") {
                NuriaBottomBar(
                    currentScreen = currentScreen,
                    language = currentLanguage,
                    onNavigate = onNavigate
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "home" -> HomeScreen(
                    branding = remoteBranding,
                    schedule = schedule,
                    location = location,
                    language = currentLanguage,
                    announcements = announcements,
                    showAd = showAds,
                    onTogglePrayerNotification = onToggleNotification,
                    onNavigateToQuran = { onNavigate("quran") },
                    onNavigateToQibla = { onNavigate("qibla") },
                    onNavigateToDua = { onNavigate("dua") },
                    onNavigateToAzkar = { onNavigate("azkar") },
                    onNavigateToTasbih = { onNavigate("tasbih") },
                    onNavigateToCalendar = { onNavigate("calendar") },
                    onNavigateToFasting = { onNavigate("fasting") },
                    onLocationClick = onLocationClick
                )
                "quran" -> QuranScreen(
                    language = currentLanguage,
                    preferencesManager = preferencesManager,
                    onSurahSelected = onOpenSurah
                )
                "surah_detail" -> SurahDetailScreen(
                    surahNumber = selectedSurahNumber,
                    language = currentLanguage,
                    preferencesManager = preferencesManager,
                    onBack = { onNavigate("quran") }
                )
                "qibla" -> QiblaScreen(
                    location = location,
                    language = currentLanguage,
                    onLocationClick = onLocationClick
                )
                "dua" -> DuaScreen(
                    language = currentLanguage,
                    preferencesManager = preferencesManager
                )
                "azkar" -> AzkarScreen(
                    language = currentLanguage
                )
                "tasbih" -> TasbihScreen(
                    language = currentLanguage,
                    preferencesManager = preferencesManager
                )
                "calendar" -> CalendarScreen(
                    language = currentLanguage
                )
                "fasting" -> FastingScreen(
                    schedule = schedule,
                    language = currentLanguage
                )
                "settings" -> SettingsScreen(
                    branding = remoteBranding,
                    language = currentLanguage,
                    preferencesManager = preferencesManager,
                    onLanguageChange = onLanguageChange,
                    onLocationChange = { onPrayerParamsChange() },
                    onPrayerParamsChange = onPrayerParamsChange
                )
            }
        }
    }
}

@Composable
fun NuriaBottomBar(
    currentScreen: String,
    language: AppLanguage,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val navItems = listOf(
            Triple("home", AppStrings.get("nav_home", language), Icons.Default.Home),
            Triple("quran", AppStrings.get("nav_quran", language), Icons.Default.MenuBook),
            Triple("qibla", AppStrings.get("nav_qibla", language), Icons.Default.Explore),
            Triple("dua", AppStrings.get("nav_dua", language), Icons.Default.Favorite),
            Triple("settings", AppStrings.get("nav_more", language), Icons.Default.Settings)
        )

        navItems.forEach { (route, label, icon) ->
            val isSelected = currentScreen == route || (route == "settings" && currentScreen in listOf("azkar", "tasbih", "calendar", "fasting"))

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IslamicEmerald,
                    selectedTextColor = IslamicEmerald,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.testTag("nav_$route")
            )
        }
    }
}
