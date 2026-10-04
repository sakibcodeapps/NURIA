package com.example.ui.screens.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.SoftGold

@Composable
fun OnboardingScreen(
    language: AppLanguage,
    onComplete: () -> Unit
) {
    var step by remember { mutableIntStateOf(0) } // 0: Welcome, 1: Location, 2: Notifications

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Proceed whether granted or denied (graceful handling)
        step = 2
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Proceed on finish
        onComplete()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("onboarding_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Skip Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Page indicator dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..2).forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == step) 24.dp else 8.dp, 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (index == step) IslamicEmerald else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        )
                    }
                }

                if (step < 2) {
                    TextButton(onClick = onComplete) {
                        Text(AppStrings.get("skip", language), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Main Step Content
            AnimatedContent(
                targetState = step,
                label = "OnboardingStep"
            ) { currentStep ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (currentStep) {
                        0 -> {
                            // Welcome Step
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(IslamicEmeraldDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mosque,
                                    contentDescription = null,
                                    tint = SoftGold,
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                text = AppStrings.get("onboarding_welcome_title", language),
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.get("onboarding_welcome_desc", language),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        1 -> {
                            // Location Permission Step
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(100.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = IslamicEmerald,
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                text = AppStrings.get("onboarding_location_title", language),
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.get("onboarding_location_desc", language),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        2 -> {
                            // Notifications Permission Step
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(100.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = IslamicEmerald,
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                text = AppStrings.get("onboarding_notification_title", language),
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.get("onboarding_notification_desc", language),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Bottom Action Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (step) {
                    0 -> {
                        Button(
                            onClick = { step = 1 },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicEmerald),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("onboarding_next_button")
                        ) {
                            Text(
                                text = AppStrings.get("continue_btn", language),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    1 -> {
                        Button(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicEmerald),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("allow_location_button")
                        ) {
                            Text(
                                text = AppStrings.get("allow_location", language),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TextButton(onClick = { step = 2 }) {
                            Text(AppStrings.get("skip", language))
                        }
                    }
                    2 -> {
                        Button(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    onComplete()
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicEmerald),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("allow_notifications_button")
                        ) {
                            Text(
                                text = AppStrings.get("allow_notifications", language),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TextButton(onClick = onComplete) {
                            Text(AppStrings.get("get_started", language))
                        }
                    }
                }
            }
        }
    }
}
