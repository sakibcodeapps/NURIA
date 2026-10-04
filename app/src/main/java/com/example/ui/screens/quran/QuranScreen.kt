package com.example.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.PreferencesManager
import com.example.core.data.QuranRepository
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.LastReadPosition
import com.example.core.model.QuranSurah
import com.example.ui.components.GlassCard
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold

@Composable
fun QuranScreen(
    language: AppLanguage,
    preferencesManager: PreferencesManager,
    onSurahSelected: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Surahs, 1: Bookmarks

    val bookmarks by preferencesManager.bookmarksFlow.collectAsState()
    val lastRead = remember { preferencesManager.getLastReadPosition() }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranRepository.surahs
        } else {
            QuranRepository.surahs.filter {
                it.number.toString().contains(searchQuery) ||
                        it.nameEnglish.contains(searchQuery, ignoreCase = true) ||
                        it.nameBangla.contains(searchQuery, ignoreCase = true) ||
                        it.nameArabic.contains(searchQuery)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("quran_screen")
    ) {
        // Search & Tab Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = AppStrings.get("search_surah", language),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IslamicEmerald,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quran_search_field")
            )

            Spacer(modifier = Modifier.height(10.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = IslamicEmerald
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = AppStrings.get("all_surahs", language),
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "${AppStrings.get("bookmarks", language)} (${bookmarks.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Last Read Quick Card (shown on All Surahs tab if search is empty)
            if (selectedTab == 0 && searchQuery.isBlank()) {
                item {
                    LastReadCard(
                        lastRead = lastRead,
                        language = language,
                        onClick = { onSurahSelected(lastRead.surahNumber) }
                    )
                }
            }

            if (selectedTab == 0) {
                items(filteredSurahs, key = { it.number }) { surah ->
                    SurahListItem(
                        surah = surah,
                        language = language,
                        onClick = { onSurahSelected(surah.number) }
                    )
                }
            } else {
                if (bookmarks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = AppStrings.get("no_data", language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(bookmarks.toList(), key = { it }) { bookmarkKey ->
                        val parts = bookmarkKey.split(":")
                        val surahNum = parts.getOrNull(0)?.toIntOrNull() ?: 1
                        val verseNum = parts.getOrNull(1)?.toIntOrNull() ?: 1
                        val surah = QuranRepository.getSurahByNumber(surahNum)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSurahSelected(surahNum) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = SoftGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = when (language) {
                                                AppLanguage.BANGLA -> surah?.nameBangla ?: "সূরা $surahNum"
                                                AppLanguage.ENGLISH -> surah?.nameEnglish ?: "Surah $surahNum"
                                                AppLanguage.ARABIC -> surah?.nameArabic ?: "سورة $surahNum"
                                            },
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${AppStrings.get("verses", language)} $verseNum",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Read",
                                    tint = IslamicEmerald
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LastReadCard(
    lastRead: LastReadPosition,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("last_read_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = IslamicMintContainer.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = IslamicEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.get("last_read", language),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = IslamicEmerald
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${lastRead.surahName} (${AppStrings.get("verses", language)} ${lastRead.verseNumber})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = AppStrings.get("continue_reading", language),
                    style = MaterialTheme.typography.labelMedium,
                    color = IslamicEmerald
                )
            }

            Surface(
                shape = CircleShape,
                color = IslamicEmerald,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Continue",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SurahListItem(
    surah: QuranSurah,
    language: AppLanguage,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("surah_item_${surah.number}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Surah Number Diamond/Circle badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (language == AppLanguage.BANGLA) com.example.core.calendar.HijriCalendarHelper.toBanglaDigits(surah.number) else surah.number.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = when (language) {
                            AppLanguage.BANGLA -> surah.nameBangla
                            AppLanguage.ENGLISH -> surah.nameEnglish
                            AppLanguage.ARABIC -> surah.nameArabic
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${surah.revelationType} • ${surah.totalVerses} ${AppStrings.get("verses", language)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Arabic calligraphy name
            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = IslamicEmerald
            )
        }
    }
}
