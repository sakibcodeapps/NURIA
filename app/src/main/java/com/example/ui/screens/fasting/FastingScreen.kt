package com.example.ui.screens.fasting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.calendar.HijriCalendarHelper
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.DailyPrayerSchedule
import com.example.ui.components.GlassCard
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold
import java.util.Date

@Composable
fun FastingScreen(
    schedule: DailyPrayerSchedule,
    language: AppLanguage
) {
    val today = remember { Date() }
    val hijri = remember(today) { HijriCalendarHelper.gregorianToHijri(today) }

    // Ramadan is the 9th month of Hijri calendar
    val isRamadan = hijri.month == 9
    val daysUntilRamadan = if (hijri.month < 9) {
        (9 - hijri.month) * 30 - hijri.day
    } else if (hijri.month > 9) {
        (12 - hijri.month + 9) * 30 - hijri.day
    } else {
        0
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("fasting_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Today's Suhoor and Iftar Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = IslamicEmeraldDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.get("fasting_title", language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SoftGold
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (isRamadan) "মাহে রমজান" else "নফল রোজা",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.NightlightRound,
                                contentDescription = null,
                                tint = SoftGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = AppStrings.get("suhoor_ends", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = schedule.formattedSuhoor,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(54.dp)
                                .background(Color.White.copy(alpha = 0.2f))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = SoftGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = AppStrings.get("iftar_time", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = schedule.formattedIftar,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Ramadan Countdown (when not yet in Ramadan)
        if (!isRamadan && daysUntilRamadan > 0) {
            item {
                GlassCard(backgroundColor = IslamicMintContainer.copy(alpha = 0.5f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NightlightRound,
                            contentDescription = null,
                            tint = IslamicEmerald,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = AppStrings.get("ramadan_countdown", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${if (language == AppLanguage.BANGLA) HijriCalendarHelper.toBanglaDigits(daysUntilRamadan) else daysUntilRamadan} " +
                                        when (language) {
                                            AppLanguage.BANGLA -> "দিন বাকি"
                                            AppLanguage.ENGLISH -> "days remaining"
                                            AppLanguage.ARABIC -> "أيام متبقية"
                                        },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = IslamicEmerald
                            )
                        }
                    }
                }
            }
        }

        // Fasting Niyyah / Intention Card
        item {
            GlassCard {
                Text(
                    text = AppStrings.get("suhoor_dua", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "نَوَيْتُ أَنْ أَصُومَ غَدًا عَنْ أَدَاءِ فَرْضِ رَمَضَانَ هَذِهِ السَّنَةِ لِلَّهِ تَعَالَى",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp, lineHeight = 34.sp),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (language) {
                        AppLanguage.BANGLA -> "অর্থ: আমি আল্লাহর সন্তুষ্টির উদ্দেশ্যে আগামীকালের রোজা রাখার নিয়ত করলাম।"
                        AppLanguage.ENGLISH -> "Meaning: I intend to fast tomorrow for the sake of Allah the Almighty."
                        AppLanguage.ARABIC -> "نية الصيام تقرباً إلى الله تعالى."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Iftar Dua Card
        item {
            GlassCard {
                Text(
                    text = AppStrings.get("iftar_dua", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp, lineHeight = 34.sp),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (language) {
                        AppLanguage.BANGLA -> "অর্থ: পিপাসা দূরীভূত হলো, শিরা-উপশিরা সিক্ত হলো এবং আল্লাহর ইচ্ছায় সওয়াব নিশ্চিত হলো।"
                        AppLanguage.ENGLISH -> "Meaning: The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills."
                        AppLanguage.ARABIC -> "المعنى: ذهب العطش وابتلت العروق واستقر الأجر إن شاء الله."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "সুনানে আবু দাউদ ২৩৫৭",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
