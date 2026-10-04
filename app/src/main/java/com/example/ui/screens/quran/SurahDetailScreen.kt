package com.example.ui.screens.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.PreferencesManager
import com.example.core.data.QuranRepository
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.QuranVerse
import com.example.ui.components.GlassCard
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    surahNumber: Int,
    language: AppLanguage,
    preferencesManager: PreferencesManager,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    val context = LocalContext.current
    val surah = remember(surahNumber) { QuranRepository.getSurahByNumber(surahNumber) }
    val verses = remember(surahNumber) { QuranRepository.getVersesForSurah(surahNumber) }
    val bookmarks by preferencesManager.bookmarksFlow.collectAsState()

    var fontSize by remember { mutableFloatStateOf(preferencesManager.getQuranFontSize()) }
    var showFontSizeControl by remember { mutableStateOf(false) }

    val showBn = preferencesManager.isShowBanglaTranslation()
    val showEn = preferencesManager.isShowEnglishTranslation()

    // Save last read position automatically
    remember(surahNumber) {
        preferencesManager.saveLastReadPosition(
            surah = surahNumber,
            verse = 1,
            name = surah?.nameEnglish ?: "Surah $surahNumber"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("surah_detail_screen")
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = when (language) {
                            AppLanguage.BANGLA -> surah?.nameBangla ?: ""
                            AppLanguage.ENGLISH -> surah?.nameEnglish ?: ""
                            AppLanguage.ARABIC -> surah?.nameArabic ?: ""
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${surah?.revelationType} • ${surah?.totalVerses} ${AppStrings.get("verses", language)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = { showFontSizeControl = !showFontSizeControl }) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Font size",
                        tint = if (showFontSizeControl) IslamicEmerald else MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        // Font Size Adjuster Drawer
        if (showFontSizeControl) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = AppStrings.get("font_size", language),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "${fontSize.toInt()} sp",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = IslamicEmerald
                        )
                    }
                    Slider(
                        value = fontSize,
                        onValueChange = {
                            fontSize = it
                            preferencesManager.saveQuranFontSize(it)
                        },
                        valueRange = 18f..38f,
                        colors = SliderDefaults.colors(
                            thumbColor = IslamicEmerald,
                            activeTrackColor = IslamicEmerald
                        )
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Surah Header Banner with Bismillah
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = IslamicMintContainer.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = surah?.nameArabic ?: "",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmerald
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (surahNumber != 9) { // At-Tawbah does not start with Bismillah
                            Text(
                                text = AppStrings.get("bismillah", language),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 36.sp
                                ),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Verses List
            items(verses, key = { it.verseNumber }) { verse ->
                val isBookmarked = preferencesManager.isVerseBookmarked(surahNumber, verse.verseNumber)

                VerseCard(
                    verse = verse,
                    fontSize = fontSize,
                    isBookmarked = isBookmarked,
                    showBangla = showBn,
                    showEnglish = showEn,
                    language = language,
                    onToggleBookmark = {
                        preferencesManager.toggleBookmark(surahNumber, verse.verseNumber)
                    },
                    onCopy = {
                        val clip = ClipData.newPlainText(
                            "Quran Verse",
                            "${verse.textArabic}\n\n${verse.textBangla}\n\n[Surah ${surah?.nameEnglish} ${surahNumber}:${verse.verseNumber}]"
                        )
                        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                        Toast.makeText(context, AppStrings.get("copied_clipboard", language), Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        val shareText = "${verse.textArabic}\n\n${verse.textBangla}\n\n${verse.textEnglish}\n\n- Surah ${surah?.nameEnglish} (${surahNumber}:${verse.verseNumber}) via NURIA"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Quran Verse"))
                    }
                )
            }
        }
    }
}

@Composable
fun VerseCard(
    verse: QuranVerse,
    fontSize: Float,
    isBookmarked: Boolean,
    showBangla: Boolean,
    showEnglish: Boolean,
    language: AppLanguage,
    onToggleBookmark: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    GlassCard {
        // Verse Number & Actions Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (language == AppLanguage.BANGLA) com.example.core.calendar.HijriCalendarHelper.toBanglaDigits(verse.verseNumber) else verse.verseNumber.toString(),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy verse",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share verse",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onToggleBookmark, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) SoftGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Arabic Verse Text (Right-aligned, large, elegant)
        Text(
            text = "${verse.textArabic} ﴿${verse.verseNumber}﴾",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = fontSize.sp,
                lineHeight = (fontSize * 1.6f).sp,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Bangla Translation
        if (showBangla) {
            Text(
                text = verse.textBangla,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // English Translation
        if (showEnglish) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = verse.textEnglish,
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
