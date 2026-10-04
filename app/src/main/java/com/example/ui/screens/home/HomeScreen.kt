package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.core.localization.AppStrings
import com.example.core.model.Announcement
import com.example.core.model.AppLanguage
import com.example.core.model.DailyPrayerSchedule
import com.example.core.model.LocationInfo
import com.example.core.model.Prayer
import com.example.core.model.PrayerTimeItem
import com.example.core.model.RemoteBranding
import com.example.ui.components.AdBannerView
import com.example.ui.components.GlassCard
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicEmeraldLight
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun HomeScreen(
    branding: RemoteBranding,
    schedule: DailyPrayerSchedule,
    location: LocationInfo,
    language: AppLanguage,
    announcements: List<Announcement>,
    showAd: Boolean = true,
    onTogglePrayerNotification: (String, Boolean) -> Unit,
    onNavigateToQuran: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToDua: () -> Unit,
    onNavigateToAzkar: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToFasting: () -> Unit,
    onLocationClick: () -> Unit
) {
    // Live ticking countdown
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    val nextPrayerTimeMillis = schedule.nextPrayer?.timeMillis ?: (currentTimeMillis + 3600000L)
    val remainingMillis = (nextPrayerTimeMillis - currentTimeMillis).coerceAtLeast(0L)
    val hours = remainingMillis / (1000 * 60 * 60)
    val minutes = (remainingMillis / (1000 * 60)) % 60
    val seconds = (remainingMillis / 1000) % 60
    val liveCountdown = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Next Prayer Card with Countdown
        item {
            HeroPrayerCard(
                schedule = schedule,
                language = language,
                countdownString = liveCountdown,
                onLocationClick = onLocationClick
            )
        }

        // Fasting Suhoor / Iftar Quick Pill
        item {
            SuhoorIftarQuickBar(
                schedule = schedule,
                language = language,
                onFastingClick = onNavigateToFasting
            )
        }

        // Quick Feature Shortcuts Grid
        item {
            Text(
                text = AppStrings.get("quick_shortcuts", language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))
            QuickShortcutsGrid(
                language = language,
                onQuran = onNavigateToQuran,
                onQibla = onNavigateToQibla,
                onDua = onNavigateToDua,
                onAzkar = onNavigateToAzkar,
                onTasbih = onNavigateToTasbih,
                onCalendar = onNavigateToCalendar,
                onFasting = onNavigateToFasting
            )
        }

        // Prayer Timetable Card
        item {
            Text(
                text = AppStrings.get("today_prayers", language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))
            PrayerTimetableCard(
                items = schedule.items,
                language = language,
                onToggleNotification = onTogglePrayerNotification
            )
        }

        // Non-intrusive Ad Banner (if enabled)
        if (showAd) {
            item {
                AdBannerView(language = language)
            }
        }

        // Announcements Section (if available)
        if (announcements.isNotEmpty()) {
            item {
                AnnouncementsSection(announcements = announcements, language = language)
            }
        }

        // Daily Ayah / Inspiration Card
        item {
            DailyInspirationCard(language = language)
        }
    }
}

@Composable
fun HeroPrayerCard(
    schedule: DailyPrayerSchedule,
    language: AppLanguage,
    countdownString: String,
    onLocationClick: () -> Unit
) {
    val nextItem = schedule.nextPrayer
    val nextPrayerName = nextItem?.prayer?.getLocalizedName(language) ?: "Fajr"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .testTag("hero_prayer_card"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = IslamicEmeraldDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background subtle hero image if available
            Image(
                painter = painterResource(id = R.drawable.nuria_hero),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                IslamicEmeraldDark.copy(alpha = 0.85f),
                                IslamicEmerald.copy(alpha = 0.95f)
                            )
                        )
                    ),
                alpha = 0.28f
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Header with Hijri and Next indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = AppStrings.get("next_prayer_in", language),
                            style = MaterialTheme.typography.labelMedium.copy(color = SoftGold, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (nextItem?.notificationEnabled == true) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                                contentDescription = "Alarm state",
                                tint = ChampagneGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Next prayer name and time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = nextPrayerName,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = nextItem?.formattedTime ?: "--:--",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = SoftGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    // Large Digital Countdown
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.35f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.4f))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = countdownString,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "HH : MM : SS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuhoorIftarQuickBar(
    schedule: DailyPrayerSchedule,
    language: AppLanguage,
    onFastingClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onFastingClick)
            .testTag("suhoor_iftar_bar"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NightlightRound,
                    contentDescription = null,
                    tint = SoftGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = AppStrings.get("suhoor_ends", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = schedule.formattedSuhoor,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(28.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = IslamicEmerald,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = AppStrings.get("iftar_time", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = schedule.formattedIftar,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun QuickShortcutsGrid(
    language: AppLanguage,
    onQuran: () -> Unit,
    onQibla: () -> Unit,
    onDua: () -> Unit,
    onAzkar: () -> Unit,
    onTasbih: () -> Unit,
    onCalendar: () -> Unit,
    onFasting: () -> Unit
) {
    data class Shortcut(val name: String, val icon: ImageVector, val onClick: () -> Unit, val tag: String)

    val shortcuts = listOf(
        Shortcut(AppStrings.get("nav_quran", language), Icons.Default.MenuBook, onQuran, "shortcut_quran"),
        Shortcut(AppStrings.get("nav_qibla", language), Icons.Default.Explore, onQibla, "shortcut_qibla"),
        Shortcut(AppStrings.get("nav_dua", language), Icons.Default.Favorite, onDua, "shortcut_dua"),
        Shortcut(AppStrings.get("azkar_title", language).take(12), Icons.Default.AutoAwesome, onAzkar, "shortcut_azkar"),
        Shortcut(AppStrings.get("tasbih_title", language), Icons.Default.TouchApp, onTasbih, "shortcut_tasbih"),
        Shortcut(AppStrings.get("islamic_calendar", language).take(12), Icons.Default.CalendarMonth, onCalendar, "shortcut_calendar"),
        Shortcut(AppStrings.get("fasting_title", language), Icons.Default.NightlightRound, onFasting, "shortcut_fasting")
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        shortcuts.take(4).forEach { shortcut ->
            ShortcutItem(shortcut.name, shortcut.icon, shortcut.tag, shortcut.onClick)
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        shortcuts.drop(4).forEach { shortcut ->
            ShortcutItem(shortcut.name, shortcut.icon, shortcut.tag, shortcut.onClick)
        }
    }
}

@Composable
fun ShortcutItem(
    title: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(4.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            modifier = Modifier.size(54.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = IslamicEmerald,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PrayerTimetableCard(
    items: List<PrayerTimeItem>,
    language: AppLanguage,
    onToggleNotification: (String, Boolean) -> Unit
) {
    GlassCard {
        items.forEachIndexed { index, item ->
            val icon = when (item.prayer) {
                Prayer.FAJR -> Icons.Default.NightlightRound
                Prayer.SUNRISE -> Icons.Default.WbSunny
                Prayer.DHUHR -> Icons.Default.WbSunny
                Prayer.ASR -> Icons.Default.WbTwilight
                Prayer.MAGHRIB -> Icons.Default.WbTwilight
                Prayer.ISHA -> Icons.Default.NightlightRound
            }

            val isCurrentNext = item.isNext
            val rowBg = if (isCurrentNext) IslamicMintContainer.copy(alpha = 0.5f) else Color.Transparent

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(rowBg)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("prayer_row_${item.prayer.id}"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isCurrentNext) IslamicEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.prayer.getLocalizedName(language),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isCurrentNext) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isCurrentNext) IslamicEmerald else MaterialTheme.colorScheme.onSurface
                        )
                        if (isCurrentNext) {
                            Text(
                                text = AppStrings.get("current_prayer", language),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = IslamicEmerald
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.formattedTime,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isCurrentNext) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = if (isCurrentNext) IslamicEmerald else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (item.prayer.isMandatoryPrayer) {
                        IconButton(
                            onClick = { onToggleNotification(item.prayer.id, !item.notificationEnabled) },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("notif_toggle_${item.prayer.id}")
                        ) {
                            Icon(
                                imageVector = if (item.notificationEnabled) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                                contentDescription = "Toggle ${item.prayer.nameEn} Notification",
                                tint = if (item.notificationEnabled) IslamicEmerald else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(32.dp))
                    }
                }
            }

            if (index < items.size - 1) {
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
fun AnnouncementsSection(
    announcements: List<Announcement>,
    language: AppLanguage
) {
    Column {
        Text(
            text = AppStrings.get("announcements", language),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
        announcements.take(2).forEach { item ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Announcement",
                        tint = if (item.isImportant) SoftGold else IslamicEmerald,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = item.getLocalizedTitle(language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.getLocalizedContent(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyInspirationCard(language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = SoftGold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = AppStrings.get("daily_inspiration", language),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = AppStrings.get("daily_ayah_text", language),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = AppStrings.get("daily_ayah_ref", language),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
