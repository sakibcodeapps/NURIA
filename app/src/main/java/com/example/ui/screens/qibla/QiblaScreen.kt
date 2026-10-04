package com.example.ui.screens.qibla

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Rotate90DegreesCcw
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.localization.AppStrings
import com.example.core.model.AppLanguage
import com.example.core.model.LocationInfo
import com.example.core.qibla.QiblaCalculator
import com.example.core.qibla.SensorManagerHelper
import com.example.ui.components.GlassCard
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicMintContainer
import com.example.ui.theme.SoftGold
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaScreen(
    location: LocationInfo,
    language: AppLanguage,
    onLocationClick: () -> Unit
) {
    val context = LocalContext.current
    val sensorHelper = remember { SensorManagerHelper(context) }

    DisposableEffect(Unit) {
        sensorHelper.startListening()
        onDispose {
            sensorHelper.stopListening()
        }
    }

    val currentHeading by sensorHelper.heading.collectAsState()
    val isSensorAvailable = sensorHelper.isSensorAvailable
    val needsCalibration by sensorHelper.needsCalibration.collectAsState()
    val accuracy by sensorHelper.accuracy.collectAsState()

    val qiblaData = remember(location, currentHeading, isSensorAvailable, accuracy, needsCalibration) {
        QiblaCalculator.computeQiblaData(
            location = location,
            currentHeading = currentHeading,
            isSensorAvailable = isSensorAvailable,
            accuracy = accuracy,
            needsCalibration = needsCalibration
        )
    }

    // Direct physical sensor response without fake animation lag
    val relativeAngle = qiblaData.relativeQiblaAngleDegrees
    val isAligned = abs(relativeAngle) < 3.5f || abs(relativeAngle - 360f) < 3.5f

    val cardinalDir = remember(currentHeading) {
        QiblaCalculator.getCardinalDirection(currentHeading)
    }
    val qiblaCardinal = remember(qiblaData.kaabaAngleDegrees) {
        QiblaCalculator.getCardinalDirection(qiblaData.kaabaAngleDegrees)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("qibla_screen"),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Location & Qibla Bearing Header
        item {
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.get("qibla_compass", language),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${location.cityName}, ${location.countryName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isAligned) IslamicMintContainer else MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = if (isAligned) "Aligned 🕋" else "${qiblaData.kaabaAngleDegrees.toInt()}° $qiblaCardinal",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isAligned) IslamicEmerald else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Sensor Unavailable Notice (Requirement 10)
        if (!isSensorAvailable) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AppStrings.get("sensor_warning", language),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // Calibration Prompt (Requirement 9: Appears only when sensor accuracy is poor or calibration needed)
        if (needsCalibration && isSensorAvailable) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.9f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Rotate90DegreesCcw,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AppStrings.get("calibrate_tip", language),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        // Dynamic Circular Compass Dial
        item {
            Box(
                modifier = Modifier
                    .size(290.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    )
                    .border(3.dp, if (isAligned) IslamicEmerald else ChampagneGold, CircleShape)
                    .shadow(10.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Rotating Compass Dial (ticks, cardinal N, E, S, W and fixed Kaaba marker on dial)
                // Rotates by -currentHeading so North on dial always points to real-world North
                val dialRotation = -currentHeading
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(dialRotation)
                ) {
                    CompassDialCanvas(
                        kaabaAngleDegrees = qiblaData.kaabaAngleDegrees
                    )
                }

                // Dynamic Qibla Needle Indicator (Requirement 4 & 5)
                // Rotates by relativeQiblaAngleDegrees = (qiblaBearing - deviceAzimuth) % 360
                // When phone is pointed towards Kaaba, relativeAngle == 0 -> points forward (up)!
                // When user turns right by 90°, relativeAngle == -90° (270°) -> points to 9 o'clock!
                Box(
                    modifier = Modifier
                        .size(210.dp)
                        .rotate(relativeAngle),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Kaaba Icon indicator attached to the tip (Requirement 13)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isAligned) IslamicEmerald else IslamicEmeraldDark,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftGold),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Mosque,
                                    contentDescription = "Kaaba Direction",
                                    tint = SoftGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Arrow shaft pointing to the Kaaba
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null,
                            tint = if (isAligned) IslamicEmerald else ChampagneGold,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                // Center Brass Pivot
                Surface(
                    shape = CircleShape,
                    color = if (isAligned) IslamicEmerald else ChampagneGold,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White),
                    modifier = Modifier.size(24.dp)
                ) {}
            }
        }

        // Live Alignment Guidance Badge
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isAligned) IslamicMintContainer else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isAligned) Icons.Default.Mosque else Icons.Default.Explore,
                        contentDescription = null,
                        tint = if (isAligned) IslamicEmerald else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAligned) {
                            when (language) {
                                AppLanguage.BANGLA -> "আপনি এখন সরাসরি পবিত্র কাবার মুখোমুখি আছেন! 🕋"
                                AppLanguage.ENGLISH -> "You are directly facing the Holy Kaaba! 🕋"
                                AppLanguage.ARABIC -> "أنت تواجه الكعبة المشرفة مباشرة الآن! 🕋"
                            }
                        } else {
                            val turnDeg = relativeAngle.toInt()
                            if (turnDeg in 1..180) {
                                when (language) {
                                    AppLanguage.BANGLA -> "ডানে ${turnDeg}° ঘুরুন"
                                    AppLanguage.ENGLISH -> "Turn right ${turnDeg}° to face Kaaba"
                                    AppLanguage.ARABIC -> "استدر يميناً بمقدار ${turnDeg}°"
                                }
                            } else {
                                val leftDeg = (360 - turnDeg) % 360
                                when (language) {
                                    AppLanguage.BANGLA -> "বামে ${leftDeg}° ঘুরুন"
                                    AppLanguage.ENGLISH -> "Turn left ${leftDeg}° to face Kaaba"
                                    AppLanguage.ARABIC -> "استدر يساراً بمقدار ${leftDeg}°"
                                }
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isAligned) IslamicEmerald else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Metrics Grid (Distance, Qibla Bearing, Current Heading)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Distance to Kaaba
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.get("distance_to_kaaba", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${qiblaData.distanceToKaabaKm.toInt()} km",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Qibla Bearing
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.get("qibla_angle", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${qiblaData.kaabaAngleDegrees.toInt()}° $qiblaCardinal",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Current Device Heading
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.get("current_heading", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${currentHeading.toInt()}° $cardinalDir",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Draws the dial face:
 * - Cardinal direction letters (N, E, S, W)
 * - Major degree tick lines (every 30°)
 * - Minor degree tick lines (every 10°)
 * - Distinct Kaaba marker at kaabaAngleDegrees on the dial rim
 */
@Composable
fun CompassDialCanvas(
    kaabaAngleDegrees: Float
) {
    Canvas(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2f

        // Outer ring
        drawCircle(
            color = Color.LightGray.copy(alpha = 0.35f),
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw degree ticks
        for (degree in 0 until 360 step 10) {
            val angleRad = Math.toRadians((degree - 90).toDouble())
            val isCardinal = degree % 90 == 0
            val isMajor = degree % 30 == 0

            val tickLength = when {
                isCardinal -> 16.dp.toPx()
                isMajor -> 10.dp.toPx()
                else -> 6.dp.toPx()
            }

            val startX = center.x + (radius - tickLength) * cos(angleRad).toFloat()
            val startY = center.y + (radius - tickLength) * sin(angleRad).toFloat()
            val endX = center.x + radius * cos(angleRad).toFloat()
            val endY = center.y + radius * sin(angleRad).toFloat()

            val tickColor = when {
                degree == 0 -> Color(0xFFD32F2F) // Red for North
                isCardinal -> Color.DarkGray
                isMajor -> Color.Gray
                else -> Color.LightGray.copy(alpha = 0.7f)
            }

            drawLine(
                color = tickColor,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = if (isCardinal) 3.dp.toPx() else if (isMajor) 2.dp.toPx() else 1.dp.toPx()
            )
        }

        // Draw Cardinal Letters (N, E, S, W) using native canvas paint
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 14.sp.toPx()
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val cardinals = listOf(0 to "N", 90 to "E", 180 to "S", 270 to "W")
        for ((deg, text) in cardinals) {
            val angleRad = Math.toRadians((deg - 90).toDouble())
            val textDist = radius - 26.dp.toPx()
            val x = center.x + textDist * cos(angleRad).toFloat()
            val y = center.y + textDist * sin(angleRad).toFloat() + (paint.textSize / 3f)

            paint.color = if (deg == 0) android.graphics.Color.RED else android.graphics.Color.GRAY
            drawContext.canvas.nativeCanvas.drawText(text, x, y, paint)
        }

        // Draw fixed Kaaba Marker on the dial at kaabaAngleDegrees
        val kaabaRad = Math.toRadians((kaabaAngleDegrees - 90).toDouble())
        val kaabaMarkerRadius = radius - 6.dp.toPx()
        val kmX = center.x + kaabaMarkerRadius * cos(kaabaRad).toFloat()
        val kmY = center.y + kaabaMarkerRadius * sin(kaabaRad).toFloat()

        drawCircle(
            color = Color(0xFFD4AF37), // SoftGold
            radius = 5.dp.toPx(),
            center = Offset(kmX, kmY)
        )
    }
}
