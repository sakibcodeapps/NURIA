package com.example.core.qibla

import com.example.core.model.LocationInfo
import com.example.core.model.QiblaData
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object QiblaCalculator {

    // Exact Kaaba coordinates as specified
    const val KAABA_LATITUDE = 21.4225
    const val KAABA_LONGITUDE = 39.8262

    /**
     * Calculates the forward geographic azimuth/bearing from user location to the Kaaba.
     * Returned bearing is in degrees [0, 360).
     */
    fun calculateQiblaAngle(latitude: Double, longitude: Double): Float {
        val userLatRad = Math.toRadians(latitude)
        val userLngRad = Math.toRadians(longitude)
        val kaabaLatRad = Math.toRadians(KAABA_LATITUDE)
        val kaabaLngRad = Math.toRadians(KAABA_LONGITUDE)

        val deltaLng = kaabaLngRad - userLngRad

        val y = sin(deltaLng) * cos(kaabaLatRad)
        val x = cos(userLatRad) * sin(kaabaLatRad) - sin(userLatRad) * cos(kaabaLatRad) * cos(deltaLng)

        val angleRad = atan2(y, x)
        var angleDeg = Math.toDegrees(angleRad).toFloat()
        angleDeg = (angleDeg % 360f + 360f) % 360f
        return angleDeg
    }

    /**
     * Computes the relative angle between the phone's top heading and the Qibla.
     * relativeAngle = (qiblaBearing - deviceAzimuth) normalized to [0, 360).
     *
     * 0° -> Qibla is straight ahead (in front).
     * 90° -> Qibla is to the right.
     * 180° -> Qibla is behind.
     * 270° -> Qibla is to the left.
     */
    fun calculateRelativeQibla(qiblaBearing: Float, deviceAzimuth: Float): Float {
        return ((qiblaBearing - deviceAzimuth) % 360f + 360f) % 360f
    }

    fun calculateDistanceToKaabaKm(latitude: Double, longitude: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(KAABA_LATITUDE - latitude)
        val dLng = Math.toRadians(KAABA_LONGITUDE - longitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latitude)) * cos(Math.toRadians(KAABA_LATITUDE)) *
                sin(dLng / 2) * sin(dLng / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    fun getCardinalDirection(heading: Float): String {
        val normalized = (heading % 360f + 360f) % 360f
        return when {
            normalized >= 337.5f || normalized < 22.5f -> "N"
            normalized in 22.5f..<67.5f -> "NE"
            normalized in 67.5f..<112.5f -> "E"
            normalized in 112.5f..<157.5f -> "SE"
            normalized in 157.5f..<202.5f -> "S"
            normalized in 202.5f..<247.5f -> "SW"
            normalized in 247.5f..<292.5f -> "W"
            else -> "NW"
        }
    }

    fun computeQiblaData(
        location: LocationInfo,
        currentHeading: Float,
        isSensorAvailable: Boolean,
        accuracy: Int = 3,
        needsCalibration: Boolean = false
    ): QiblaData {
        val kaabaAngle = calculateQiblaAngle(location.latitude, location.longitude)
        val relative = calculateRelativeQibla(kaabaAngle, currentHeading)
        val distance = calculateDistanceToKaabaKm(location.latitude, location.longitude)

        return QiblaData(
            kaabaAngleDegrees = kaabaAngle,
            compassHeadingDegrees = currentHeading,
            relativeQiblaAngleDegrees = relative,
            distanceToKaabaKm = distance,
            isSensorAvailable = isSensorAvailable,
            accuracy = accuracy,
            needsCalibration = needsCalibration
        )
    }
}
