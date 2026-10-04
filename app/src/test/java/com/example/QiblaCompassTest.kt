package com.example

import com.example.core.model.LocationInfo
import com.example.core.qibla.QiblaCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class QiblaCompassTest {

    // Test coordinates: Dhaka, Bangladesh
    private val dhakaLocation = LocationInfo(
        latitude = 23.8103,
        longitude = 90.4125,
        cityName = "Dhaka",
        countryName = "Bangladesh"
    )

    // London, UK
    private val londonLocation = LocationInfo(
        latitude = 51.5074,
        longitude = -0.1278,
        cityName = "London",
        countryName = "UK"
    )

    @Test
    fun testQiblaAngleCalculationForBangladesh() {
        val qiblaAngle = QiblaCalculator.calculateQiblaAngle(dhakaLocation.latitude, dhakaLocation.longitude)
        // From Dhaka, Kaaba is approximately 277.6° (West-North-West)
        assertTrue("Qibla angle from Dhaka should be ~277°-278°, got $qiblaAngle", qiblaAngle in 276f..279f)
    }

    @Test
    fun testQiblaAngleCalculationForLondon() {
        val qiblaAngle = QiblaCalculator.calculateQiblaAngle(londonLocation.latitude, londonLocation.longitude)
        // From London, Kaaba is approximately 118.9° (East-South-East)
        assertTrue("Qibla angle from London should be ~118°-120°, got $qiblaAngle", qiblaAngle in 118f..120f)
    }

    @Test
    fun testDistanceToKaaba() {
        val distanceDhaka = QiblaCalculator.calculateDistanceToKaabaKm(dhakaLocation.latitude, dhakaLocation.longitude)
        // Distance Dhaka to Makkah is ~5100-5200 km
        assertTrue("Distance should be ~5100-5200 km, got $distanceDhaka", distanceDhaka in 5000.0..5300.0)
    }

    @Test
    fun testCaseA_QiblaInFrontOfUser() {
        // When user faces the Qibla directly: deviceAzimuth == qiblaBearing
        val qiblaBearing = 277.6f
        val deviceAzimuth = 277.6f
        val relative = QiblaCalculator.calculateRelativeQibla(qiblaBearing, deviceAzimuth)

        // Indicator should point directly forward (0°)
        assertEquals(0f, relative, 0.01f)
    }

    @Test
    fun testCaseB_QiblaBehindUser() {
        // When user faces away from Qibla (opposite direction): deviceAzimuth == (qiblaBearing + 180) % 360
        val qiblaBearing = 277.6f
        val deviceAzimuth = (277.6f + 180f) % 360f // 97.6°
        val relative = QiblaCalculator.calculateRelativeQibla(qiblaBearing, deviceAzimuth)

        // Indicator should point directly behind (180°)
        assertEquals(180f, relative, 0.01f)
    }

    @Test
    fun testCaseC_UserRotates90DegreesClockwise() {
        // Initially facing North (Azimuth = 0°)
        val qiblaBearing = 278f
        val initialAzimuth = 0f
        val initialRelative = QiblaCalculator.calculateRelativeQibla(qiblaBearing, initialAzimuth)
        assertEquals(278f, initialRelative, 0.01f) // pointing ~9 o'clock (left)

        // User rotates 90° clockwise to face East (Azimuth = 90°)
        val newAzimuth = 90f
        val updatedRelative = QiblaCalculator.calculateRelativeQibla(qiblaBearing, newAzimuth)
        // (278 - 90) = 188° (rear-left)
        assertEquals(188f, updatedRelative, 0.01f)

        // Difference is exactly 90 degrees
        val diff = (initialRelative - updatedRelative + 360f) % 360f
        assertEquals(90f, diff, 0.01f)
    }

    @Test
    fun testCaseD_UserRotates180Degrees() {
        val qiblaBearing = 278f
        val initialAzimuth = 45f // facing North-East
        val initialRelative = QiblaCalculator.calculateRelativeQibla(qiblaBearing, initialAzimuth) // 233°

        // User turns 180° around (facing South-West = 225°)
        val newAzimuth = 225f
        val updatedRelative = QiblaCalculator.calculateRelativeQibla(qiblaBearing, newAzimuth) // 53°

        val diff = abs(initialRelative - updatedRelative)
        assertEquals(180f, diff, 0.01f)
    }

    @Test
    fun testCaseE_ContinuousRotationTracking() {
        val qiblaBearing = 277.6f

        // Simulate a complete 360-degree rotation step-by-step
        for (azimuthDeg in 0 until 360 step 15) {
            val relative = QiblaCalculator.calculateRelativeQibla(qiblaBearing, azimuthDeg.toFloat())
            // Always normalized in [0, 360)
            assertTrue("Relative angle must be in [0, 360), got $relative", relative >= 0f && relative < 360f)

            // The sum of relative + azimuth must reconstruct qiblaBearing modulo 360
            val reconstructed = (relative + azimuthDeg.toFloat()) % 360f
            assertEquals("Reconstruction must match qibla bearing", qiblaBearing, reconstructed, 0.01f)
        }
    }

    @Test
    fun testNormalizationBounds() {
        // Negative inputs
        val rel1 = QiblaCalculator.calculateRelativeQibla(10f, 350f) // 10 - 350 = -340 = 20
        assertEquals(20f, rel1, 0.01f)

        val rel2 = QiblaCalculator.calculateRelativeQibla(0f, 0f)
        assertEquals(0f, rel2, 0.01f)

        val rel3 = QiblaCalculator.calculateRelativeQibla(359f, 0f)
        assertEquals(359f, rel3, 0.01f)
    }

    @Test
    fun testCardinalDirectionHelper() {
        assertEquals("N", QiblaCalculator.getCardinalDirection(0f))
        assertEquals("NE", QiblaCalculator.getCardinalDirection(45f))
        assertEquals("E", QiblaCalculator.getCardinalDirection(90f))
        assertEquals("SE", QiblaCalculator.getCardinalDirection(135f))
        assertEquals("S", QiblaCalculator.getCardinalDirection(180f))
        assertEquals("SW", QiblaCalculator.getCardinalDirection(225f))
        assertEquals("W", QiblaCalculator.getCardinalDirection(270f))
        assertEquals("NW", QiblaCalculator.getCardinalDirection(315f))
        assertEquals("N", QiblaCalculator.getCardinalDirection(359f))
    }
}
