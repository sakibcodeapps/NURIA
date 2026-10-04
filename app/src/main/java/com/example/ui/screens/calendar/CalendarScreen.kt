package com.example.ui.screens.calendar

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.calendar.HijriCalendarHelper
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.ui.components.GlassCard
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(language: AppLanguage) {
    val today = remember { Date() }
    val hijriDate = remember(today) { HijriCalendarHelper.gregorianToHijri(today) }
    val formattedHijri = remember(today, language) { HijriCalendarHelper.getFormattedHijriDate(today, language) }
    val gregFormat = remember { SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("calendar_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Today's Date Banner Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = IslamicEmeraldDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = SoftGold,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = formattedHijri,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = gregFormat.format(today),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = SoftGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // Section Title: Key Islamic Events
        item {
            Text(
                text = AppStrings.get("islamic_events", language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(HijriCalendarHelper.ISLAMIC_EVENTS, key = { it.id }) { event ->
            val monthName = HijriCalendarHelper.getMonthName(event.hijriMonth, language)
            val dayStr = if (language == AppLanguage.BANGLA) HijriCalendarHelper.toBanglaDigits(event.hijriDay) else event.hijriDay.toString()

            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = IslamicMintContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = dayStr,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = IslamicEmerald
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.getLocalizedTitle(language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$dayStr $monthName",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = SoftGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.getLocalizedDescription(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
