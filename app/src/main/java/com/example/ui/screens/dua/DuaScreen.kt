package com.example.ui.screens.dua

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.DuaRepository
import com.example.core.data.PreferencesManager
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.DuaItem
import com.example.ui.components.GlassCard
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.SoftGold

@Composable
fun DuaScreen(
    language: AppLanguage,
    preferencesManager: PreferencesManager
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<String?>("all") }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val favorites by preferencesManager.favoriteDuasFlow.collectAsState()

    val filteredDuas = remember(searchQuery, selectedCategoryId, showOnlyFavorites, favorites) {
        DuaRepository.duas.filter { item ->
            val matchesCategory = selectedCategoryId == "all" || item.categoryId == selectedCategoryId
            val matchesSearch = searchQuery.isBlank() ||
                    item.titleBn.contains(searchQuery, ignoreCase = true) ||
                    item.titleEn.contains(searchQuery, ignoreCase = true) ||
                    item.titleAr.contains(searchQuery) ||
                    item.contentBn.contains(searchQuery, ignoreCase = true) ||
                    item.contentEn.contains(searchQuery, ignoreCase = true) ||
                    item.reference.contains(searchQuery, ignoreCase = true)
            val matchesFavorite = !showOnlyFavorites || favorites.contains(item.id)

            matchesCategory && matchesSearch && matchesFavorite
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dua_screen")
    ) {
        // Search & Category Filters
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(text = AppStrings.get("search_dua", language), style = MaterialTheme.typography.bodyMedium)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
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
                    .testTag("dua_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategoryId == "all" && !showOnlyFavorites,
                        onClick = {
                            selectedCategoryId = "all"
                            showOnlyFavorites = false
                        },
                        label = { Text(AppStrings.get("all_categories", language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IslamicEmerald,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }

                item {
                    FilterChip(
                        selected = showOnlyFavorites,
                        onClick = { showOnlyFavorites = !showOnlyFavorites },
                        label = { Text("${AppStrings.get("favorites", language)} (${favorites.size})") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (showOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftGold,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.Black
                        )
                    )
                }

                items(DuaRepository.categories, key = { it.id }) { cat ->
                    FilterChip(
                        selected = selectedCategoryId == cat.id && !showOnlyFavorites,
                        onClick = {
                            selectedCategoryId = cat.id
                            showOnlyFavorites = false
                        },
                        label = { Text(cat.getLocalizedName(language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IslamicEmerald,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (filteredDuas.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.get("no_data", language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filteredDuas, key = { it.id }) { item ->
                    val isFav = favorites.contains(item.id)

                    DuaCard(
                        item = item,
                        language = language,
                        isFavorite = isFav,
                        onToggleFavorite = { preferencesManager.toggleFavoriteDua(item.id) },
                        onCopy = {
                            val clip = ClipData.newPlainText(
                                "Dua",
                                "${item.getLocalizedTitle(language)}\n\n${item.contentAr}\n\n${item.getLocalizedContent(language)}\n\nসূত্র: ${item.reference}"
                            )
                            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                            Toast.makeText(context, AppStrings.get("copied_clipboard", language), Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val shareText = "${item.getLocalizedTitle(language)}\n\n${item.contentAr}\n\n${item.contentBn}\n\n${item.contentEn}\n\nসূত্র: ${item.reference}\n(via NURIA App)"
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Dua"))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DuaCard(
    item: DuaItem,
    language: AppLanguage,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
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

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
                IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) androidx.compose.ui.graphics.Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Arabic Dua Text
        Text(
            text = item.contentAr,
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

        // Localized Meaning
        Text(
            text = item.getLocalizedContent(language),
            style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Reference
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
    }
}
