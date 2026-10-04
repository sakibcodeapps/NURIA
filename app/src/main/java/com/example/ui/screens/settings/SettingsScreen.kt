package com.example.ui.screens.settings

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.PreferencesManager
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.CalculationMethod
import com.example.core.model.LocationInfo
import com.example.core.model.Madhab
import com.example.core.model.RemoteBranding
import com.example.ui.components.GlassCard
import com.example.ui.theme.IslamicEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    branding: RemoteBranding,
    language: AppLanguage,
    preferencesManager: PreferencesManager,
    onLanguageChange: (AppLanguage) -> Unit,
    onLocationChange: (LocationInfo) -> Unit,
    onPrayerParamsChange: () -> Unit
) {
    var showCityDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    var selectedCalcMethod by remember {
        mutableStateOf(preferencesManager.getPrayerCalculationParams().method)
    }
    var selectedMadhab by remember {
        mutableStateOf(preferencesManager.getPrayerCalculationParams().madhab)
    }

    var calcMethodExpanded by remember { mutableStateOf(false) }
    var madhabExpanded by remember { mutableStateOf(false) }

    var allNotifEnabled by remember { mutableStateOf(preferencesManager.isAllNotificationsEnabled()) }
    var quranFontSize by remember { mutableFloatStateOf(preferencesManager.getQuranFontSize()) }
    var showBnTrans by remember { mutableStateOf(preferencesManager.isShowBanglaTranslation()) }
    var showEnTrans by remember { mutableStateOf(preferencesManager.isShowEnglishTranslation()) }

    var themeMode by remember { mutableIntStateOf(preferencesManager.getThemeMode()) } // 0: System, 1: Light, 2: Dark

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Language Selector Section
        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = IslamicEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.get("language_setting", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppLanguage.entries.forEach { lang ->
                        val isSelected = lang == language
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onLanguageChange(lang) }
                                .testTag("lang_select_${lang.code}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) IslamicEmerald else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = lang.nativeName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = lang.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Prayer Calculation Method & Madhab Section
        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = IslamicEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.get("prayer_settings", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Calculation Method Dropdown
                Text(
                    text = AppStrings.get("calc_method", language),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = calcMethodExpanded,
                    onExpandedChange = { calcMethodExpanded = !calcMethodExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCalcMethod.getLocalizedName(language),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = calcMethodExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = calcMethodExpanded,
                        onDismissRequest = { calcMethodExpanded = false }
                    ) {
                        CalculationMethod.entries.forEach { method ->
                            DropdownMenuItem(
                                text = { Text(method.getLocalizedName(language)) },
                                onClick = {
                                    selectedCalcMethod = method
                                    preferencesManager.saveCalculationMethod(method)
                                    onPrayerParamsChange()
                                    calcMethodExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Madhab Dropdown (Asr)
                Text(
                    text = AppStrings.get("madhab", language),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = madhabExpanded,
                    onExpandedChange = { madhabExpanded = !madhabExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedMadhab.getLocalizedName(language),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = madhabExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = madhabExpanded,
                        onDismissRequest = { madhabExpanded = false }
                    ) {
                        Madhab.entries.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m.getLocalizedName(language)) },
                                onClick = {
                                    selectedMadhab = m
                                    preferencesManager.saveMadhab(m)
                                    onPrayerParamsChange()
                                    madhabExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Location Settings Section
        item {
            val location = preferencesManager.getLocation()

            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = IslamicEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.get("location_settings", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${location.cityName}, ${location.countryName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Lat: ${String.format("%.4f", location.latitude)}, Lng: ${String.format("%.4f", location.longitude)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    TextButton(onClick = { showCityDialog = true }) {
                        Text(AppStrings.get("select_city", language), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Notifications & Adhan Section
        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = IslamicEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.get("notifications", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.get("enable_all_notifications", language),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = allNotifEnabled,
                        onCheckedChange = {
                            allNotifEnabled = it
                            preferencesManager.setAllNotificationsEnabled(it)
                            onPrayerParamsChange()
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicEmerald)
                    )
                }
            }
        }

        // Quran Reading Preferences Section
        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = IslamicEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.get("holy_quran", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${AppStrings.get("font_size", language)}: ${quranFontSize.toInt()} sp",
                    style = MaterialTheme.typography.labelMedium
                )
                Slider(
                    value = quranFontSize,
                    onValueChange = {
                        quranFontSize = it
                        preferencesManager.saveQuranFontSize(it)
                    },
                    valueRange = 18f..38f,
                    colors = SliderDefaults.colors(thumbColor = IslamicEmerald, activeTrackColor = IslamicEmerald)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.get("translation_bn", language), style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = showBnTrans,
                        onCheckedChange = {
                            showBnTrans = it
                            preferencesManager.setShowBanglaTranslation(it)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicEmerald)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.get("translation_en", language), style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = showEnTrans,
                        onCheckedChange = {
                            showEnTrans = it
                            preferencesManager.setShowEnglishTranslation(it)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicEmerald)
                    )
                }
            }
        }

        // About & Legal
        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = IslamicEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.get("about_app", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = branding.appName,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = branding.tagline,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${AppStrings.get("version", language)}: 1.0.0 (Production Release)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextButton(onClick = { showPrivacyDialog = true }) {
                        Text(AppStrings.get("privacy_policy", language))
                    }
                    TextButton(onClick = { showTermsDialog = true }) {
                        Text(AppStrings.get("terms_of_service", language))
                    }
                }
            }
        }
    }

    // City Selection Dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text(AppStrings.get("select_city", language), fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(PreferencesManager.PRESET_CITIES) { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    preferencesManager.saveLocation(city)
                                    onLocationChange(city)
                                    showCityDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = city.cityName, fontWeight = FontWeight.SemiBold)
                            Text(text = city.countryName, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text(AppStrings.get("close", language))
                }
            }
        )
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text(AppStrings.get("privacy_policy", language), fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "NURIA values your privacy. Your GPS location is solely used on-device to calculate precise local prayer timings and the Qibla azimuth angle. No personal prayer habits, location telemetry, or personal identity records are collected or shared with third parties."
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text(AppStrings.get("close", language))
                }
            }
        )
    }

    // Terms of Service Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text(AppStrings.get("terms_of_service", language), fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "NURIA provides verified prayer schedules based on established astronomical equations and recognized Islamic authorities. For extreme northern latitudes or local regional variations, please consult your local mosque committee."
                )
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text(AppStrings.get("close", language))
                }
            }
        )
    }
}
