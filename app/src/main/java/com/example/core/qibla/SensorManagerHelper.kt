package com.example.core.qibla

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.Surface
import android.view.WindowManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class SensorManagerHelper(private val context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private val rotationVectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val geomagneticSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    @Suppress("DEPRECATION")
    private val orientationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

    val isSensorAvailable: Boolean = rotationVectorSensor != null ||
            geomagneticSensor != null ||
            (accelerometer != null && magnetometer != null) ||
            orientationSensor != null

    private val _heading = MutableStateFlow(0f)
    val heading: StateFlow<Float> = _heading.asStateFlow()

    private val _accuracy = MutableStateFlow(SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
    val accuracy: StateFlow<Int> = _accuracy.asStateFlow()

    private val _needsCalibration = MutableStateFlow(false)
    val needsCalibration: StateFlow<Boolean> = _needsCalibration.asStateFlow()

    private val gravityValues = FloatArray(3)
    private val magneticValues = FloatArray(3)
    private var hasGravity = false
    private var hasMagnetic = false

    private val rotationMatrix = FloatArray(9)
    private val remappedMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var lastRotationVectorTimestamp = 0L
    private var lastGeomagneticTimestamp = 0L

    // Circular trigonometric smoothing state
    private var smoothSin = 0.0
    private var smoothCos = 1.0
    private var hasInitialHeading = false

    fun startListening() {
        if (sensorManager == null) return

        // Register available sensors using SENSOR_DELAY_GAME for responsive, real-time tracking
        rotationVectorSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        geomagneticSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        orientationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        // Check calibration requirement from event accuracy
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE ||
            event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW
        ) {
            _needsCalibration.value = true
        } else if (event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM ||
            event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH
        ) {
            _needsCalibration.value = false
        }

        val now = System.currentTimeMillis()

        when (event.sensor.type) {
            Sensor.TYPE_ROTATION_VECTOR -> {
                lastRotationVectorTimestamp = now
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                computeAndEmitAzimuth()
            }
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> {
                // Use if primary rotation vector has not emitted recently
                if (now - lastRotationVectorTimestamp > 400L) {
                    lastGeomagneticTimestamp = now
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    computeAndEmitAzimuth()
                }
            }
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, gravityValues, 0, 3)
                hasGravity = true
                if (now - lastRotationVectorTimestamp > 400L && now - lastGeomagneticTimestamp > 400L) {
                    computeFromAccelAndMag()
                }
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, magneticValues, 0, 3)
                hasMagnetic = true
                if (now - lastRotationVectorTimestamp > 400L && now - lastGeomagneticTimestamp > 400L) {
                    computeFromAccelAndMag()
                }
            }
            @Suppress("DEPRECATION")
            Sensor.TYPE_ORIENTATION -> {
                // Fallback for virtual emulators that only provide legacy orientation
                if (now - lastRotationVectorTimestamp > 600L && now - lastGeomagneticTimestamp > 600L && (!hasGravity || !hasMagnetic)) {
                    val rawAzimuth = event.values[0]
                    updateSmoothedHeading(rawAzimuth)
                }
            }
        }
    }

    private fun computeFromAccelAndMag() {
        if (!hasGravity || !hasMagnetic) return
        val success = SensorManager.getRotationMatrix(rotationMatrix, null, gravityValues, magneticValues)
        if (success) {
            computeAndEmitAzimuth()
        }
    }

    private fun computeAndEmitAzimuth() {
        val displayRotation = getDisplayRotation()

        var axisX = SensorManager.AXIS_X
        var axisY = SensorManager.AXIS_Y

        when (displayRotation) {
            Surface.ROTATION_0 -> {
                axisX = SensorManager.AXIS_X
                axisY = SensorManager.AXIS_Y
            }
            Surface.ROTATION_90 -> {
                axisX = SensorManager.AXIS_Y
                axisY = SensorManager.AXIS_MINUS_X
            }
            Surface.ROTATION_180 -> {
                axisX = SensorManager.AXIS_MINUS_X
                axisY = SensorManager.AXIS_MINUS_Y
            }
            Surface.ROTATION_270 -> {
                axisX = SensorManager.AXIS_MINUS_Y
                axisY = SensorManager.AXIS_X
            }
        }

        val success = SensorManager.remapCoordinateSystem(rotationMatrix, axisX, axisY, remappedMatrix)
        val matrixToUse = if (success) remappedMatrix else rotationMatrix

        SensorManager.getOrientation(matrixToUse, orientationAngles)
        val azimuthRad = orientationAngles[0]
        var azimuthDeg = Math.toDegrees(azimuthRad.toDouble()).toFloat()
        azimuthDeg = (azimuthDeg % 360f + 360f) % 360f

        updateSmoothedHeading(azimuthDeg)
    }

    private fun getDisplayRotation(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                context.display?.rotation ?: Surface.ROTATION_0
            } catch (_: Exception) {
                Surface.ROTATION_0
            }
        } else {
            @Suppress("DEPRECATION")
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            @Suppress("DEPRECATION")
            wm?.defaultDisplay?.rotation ?: Surface.ROTATION_0
        }
    }

    /**
     * Circular trigonometric filter:
     * Maintains running weighted averages of sin and cos to avoid 0°/360° discontinuity.
     * Uses adaptive alpha: fast response when turning quickly, fine filtering when stationary.
     */
    private fun updateSmoothedHeading(newHeadingDeg: Float) {
        val normalizedHeading = (newHeadingDeg % 360f + 360f) % 360f
        val newRad = Math.toRadians(normalizedHeading.toDouble())
        val newSin = sin(newRad)
        val newCos = cos(newRad)

        if (!hasInitialHeading) {
            smoothSin = newSin
            smoothCos = newCos
            hasInitialHeading = true
            _heading.value = normalizedHeading
            return
        }

        val currentHeading = _heading.value
        var diff = abs(normalizedHeading - currentHeading)
        if (diff > 180f) diff = 360f - diff

        // Adaptive alpha: quick response during active turning, gentle filter for micro-jitter
        val alpha = when {
            diff > 35f -> 0.85
            diff > 15f -> 0.60
            diff > 5f -> 0.35
            else -> 0.20
        }

        smoothSin = alpha * newSin + (1.0 - alpha) * smoothSin
        smoothCos = alpha * newCos + (1.0 - alpha) * smoothCos

        var smoothedDeg = Math.toDegrees(atan2(smoothSin, smoothCos)).toFloat()
        smoothedDeg = (smoothedDeg % 360f + 360f) % 360f
        _heading.value = smoothedDeg
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        _accuracy.value = accuracy
        _needsCalibration.value = (accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE ||
                accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW)
    }
}
