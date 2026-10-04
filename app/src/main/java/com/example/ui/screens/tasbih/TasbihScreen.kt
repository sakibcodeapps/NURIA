package com.example.ui.screens.tasbih

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.PreferencesManager
import com.example.core.data.TasbihRepository
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.TasbihPreset
import com.example.ui.components.GlassCard
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold

@Composable
fun TasbihScreen(
    language: AppLanguage,
    preferencesManager: PreferencesManager
) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }
    val toneGen = remember {
        try {
            ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60)
        } catch (_: Exception) {
            null
        }
    }

    var count by remember { mutableIntStateOf(preferencesManager.getTasbihCount()) }
    var target by remember { mutableIntStateOf(preferencesManager.getTasbihTarget()) }
    var isVibrateEnabled by remember { mutableStateOf(preferencesManager.isTasbihVibration()) }
    var isSoundEnabled by remember { mutableStateOf(preferencesManager.isTasbihSound()) }

    var selectedPreset by remember { mutableStateOf(TasbihRepository.defaultPresets.first()) }

    val progress = if (target > 0) (count.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "TasbihProgress")

    fun triggerTap() {
        count++
        preferencesManager.saveTasbihCount(count)

        if (isVibrateEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val duration = if (count % target == 0) 120L else 35L
                vibrator?.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(35)
            }
        }

        if (isSoundEnabled) {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("tasbih_screen"),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dhikr Presets Carousel
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(TasbihRepository.defaultPresets, key = { it.id }) { preset ->
                    FilterChip(
                        selected = selectedPreset.id == preset.id,
                        onClick = {
                            selectedPreset = preset
                            target = preset.target
                            preferencesManager.saveTasbihTarget(target)
                        },
                        label = { Text(preset.getLocalizedName(language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IslamicEmerald,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Active Dhikr Display
        item {
            GlassCard(
                backgroundColor = IslamicMintContainer.copy(alpha = 0.4f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = selectedPreset.textAr,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicEmerald
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedPreset.getLocalizedName(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Huge Tactile Counter Ring
        item {
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    )
                    .border(4.dp, ChampagneGold.copy(alpha = 0.5f), CircleShape)
                    .shadow(10.dp, CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { triggerTap() }
                    .testTag("tasbih_tap_button"),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(250.dp),
                    color = IslamicEmerald,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    strokeWidth = 8.dp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.BANGLA) com.example.core.calendar.HijriCalendarHelper.toBanglaDigits(count) else count.toString(),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "${AppStrings.get("target", language)}: ${if (language == AppLanguage.BANGLA) com.example.core.calendar.HijriCalendarHelper.toBanglaDigits(target) else target.toString()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = AppStrings.get("tap_to_count", language),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = SoftGold
                    )
                }
            }
        }

        // Target Selector Buttons (33, 99, 100, 1000)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(33, 99, 100, 1000).forEach { tVal ->
                    FilterChip(
                        selected = target == tVal,
                        onClick = {
                            target = tVal
                            preferencesManager.saveTasbihTarget(tVal)
                        },
                        label = {
                            Text(
                                if (language == AppLanguage.BANGLA) com.example.core.calendar.HijriCalendarHelper.toBanglaDigits(tVal) else tVal.toString()
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftGold,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }
        }

        // Controls: Reset, Vibrate, Sound
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reset Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                count = 0
                                preferencesManager.saveTasbihCount(0)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.get("reset", language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Toggles for Sound & Vibration
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                isVibrateEnabled = !isVibrateEnabled
                                preferencesManager.setTasbihVibration(isVibrateEnabled)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Vibration,
                                contentDescription = "Toggle Vibration",
                                tint = if (isVibrateEnabled) IslamicEmerald else MaterialTheme.colorScheme.outline
                            )
                        }

                        IconButton(
                            onClick = {
                                isSoundEnabled = !isSoundEnabled
                                preferencesManager.setTasbihSound(isSoundEnabled)
                            }
                        ) {
                            Icon(
                                imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                                contentDescription = "Toggle Sound",
                                tint = if (isSoundEnabled) IslamicEmerald else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    }
}
