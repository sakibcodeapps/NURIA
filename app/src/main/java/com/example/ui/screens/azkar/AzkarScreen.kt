package com.example.ui.screens.azkar

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.AzkarRepository
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.AzkarItem
import com.example.ui.components.GlassCard
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold

@Composable
fun AzkarScreen(language: AppLanguage) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }

    var selectedCategory by remember { mutableStateOf("morning") }
    val countsMap = remember { mutableStateMapOf<String, Int>() }

    val categories = listOf(
        "morning" to AppStrings.get("morning_azkar", language),
        "evening" to AppStrings.get("evening_azkar", language),
        "after_prayer" to AppStrings.get("after_prayer_azkar", language),
        "sleep" to AppStrings.get("sleep_azkar", language)
    )

    val currentItems = remember(selectedCategory) {
        AzkarRepository.azkarList.filter { it.categoryId == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("azkar_screen")
    ) {
        // Category Pills
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { (id, title) ->
                FilterChip(
                    selected = selectedCategory == id,
                    onClick = { selectedCategory = id },
                    label = { Text(title) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IslamicEmerald,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(currentItems, key = { it.id }) { item ->
                val currentCount = countsMap[item.id] ?: 0
                val isCompleted = currentCount >= item.targetCount

                AzkarCard(
                    item = item,
                    language = language,
                    currentCount = currentCount,
                    isCompleted = isCompleted,
                    onCountClick = {
                        val newCount = (currentCount + 1).coerceAtMost(item.targetCount)
                        countsMap[item.id] = newCount

                        // Haptic feedback
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator?.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            @Suppress("DEPRECATION")
                            vibrator?.vibrate(35)
                        }
                    },
                    onCopy = {
                        val clip = ClipData.newPlainText(
                            "Azkar",
                            "${item.getLocalizedTitle(language)}\n\n${item.textAr}\n\n${item.getLocalizedText(language)}\n\nসূত্র: ${item.reference}"
                        )
                        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                        Toast.makeText(context, AppStrings.get("copied_clipboard", language), Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        val shareText = "${item.getLocalizedTitle(language)}\n\n${item.textAr}\n\n${item.getLocalizedText(language)}\n\nসূত্র: ${item.reference}\n(via NURIA)"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Azkar"))
                    }
                )
            }
        }
    }
}

@Composable
fun AzkarCard(
    item: AzkarItem,
    language: AppLanguage,
    currentCount: Int,
    isCompleted: Boolean,
    onCountClick: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    GlassCard {
        // Title & Actions Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.getLocalizedTitle(language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Row {
                IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Arabic Text
        Text(
            text = item.textAr,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 22.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Translation
        Text(
            text = item.getLocalizedText(language),
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Benefit / Reward
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = IslamicMintContainer.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "✨ ${item.getLocalizedBenefit(language)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Counter Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Text(
                    text = item.reference,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Button(
                onClick = onCountClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) IslamicEmerald else IslamicEmeraldDark
                ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "${item.targetCount} / ${item.targetCount}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "$currentCount / ${item.targetCount}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
