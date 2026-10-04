package com.example.core.prayer

import com.example.core.model.CalculationMethod
import com.example.core.model.DailyPrayerSchedule
import com.example.core.model.LocationInfo
import com.example.core.model.Madhab
import com.example.core.model.Prayer
import com.example.core.model.PrayerTimeItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

object PrayerCalculator {

    data class PrayerCalculationParameters(
        val method: CalculationMethod = CalculationMethod.KARACHI,
        val madhab: Madhab = Madhab.HANAFI,
        val fajrOffsetMinutes: Int = 0,
        val sunriseOffsetMinutes: Int = 0,
        val dhuhrOffsetMinutes: Int = 0,
        val asrOffsetMinutes: Int = 0,
        val maghribOffsetMinutes: Int = 0,
        val ishaOffsetMinutes: Int = 0
    )

    fun calculateDailySchedule(
        date: Date,
        location: LocationInfo,
        params: PrayerCalculationParameters = PrayerCalculationParameters(),
        enabledNotifications: Map<String, Boolean> = emptyMap()
    ): DailyPrayerSchedule {
        val calendar = Calendar.getInstance().apply { time = date }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val tz = TimeZone.getDefault()
        val timeZoneOffsetHours = tz.getOffset(date.time) / 3600000.0

        val timesMap = computePrayerTimesForDay(
            year, month, day,
            location.latitude, location.longitude,
            timeZoneOffsetHours,
            params
        )

        val baseCal = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val prayerItems = mutableListOf<PrayerTimeItem>()
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val now = System.currentTimeMillis()

        for (prayer in Prayer.entries) {
            val hourDecimal = timesMap[prayer] ?: 12.0
            val hours = hourDecimal.toInt()
            val minutes = ((hourDecimal - hours) * 60).toInt()

            val pCal = (baseCal.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, hours)
                set(Calendar.MINUTE, minutes)
            }
            val timeMillis = pCal.timeInMillis
            val isPassed = now > timeMillis
            val isEnabled = enabledNotifications[prayer.id] ?: true

            prayerItems.add(
                PrayerTimeItem(
                    prayer = prayer,
                    timeMillis = timeMillis,
                    formattedTime = timeFormat.format(Date(timeMillis)),
                    isNext = false,
                    isPassed = isPassed,
                    notificationEnabled = isEnabled
                )
            )
        }

        // Determine next prayer
        val nextCandidate = prayerItems.firstOrNull { it.prayer.isMandatoryPrayer && !it.isPassed }
            ?: run {
                // If all passed today, next is Fajr tomorrow
                val tomorrowCal = (baseCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
                val tomorrowSchedule = computePrayerTimesForDay(
                    tomorrowCal.get(Calendar.YEAR),
                    tomorrowCal.get(Calendar.MONTH) + 1,
                    tomorrowCal.get(Calendar.DAY_OF_MONTH),
                    location.latitude, location.longitude,
                    timeZoneOffsetHours,
                    params
                )
                val fajrTomorrowHour = tomorrowSchedule[Prayer.FAJR] ?: 5.0
                val h = fajrTomorrowHour.toInt()
                val m = ((fajrTomorrowHour - h) * 60).toInt()
                tomorrowCal.set(Calendar.HOUR_OF_DAY, h)
                tomorrowCal.set(Calendar.MINUTE, m)
                PrayerTimeItem(
                    prayer = Prayer.FAJR,
                    timeMillis = tomorrowCal.timeInMillis,
                    formattedTime = timeFormat.format(Date(tomorrowCal.timeInMillis)),
                    isNext = true,
                    isPassed = false,
                    notificationEnabled = enabledNotifications[Prayer.FAJR.id] ?: true
                )
            }

        val updatedItems = prayerItems.map {
            if (it.prayer == nextCandidate.prayer && it.timeMillis == nextCandidate.timeMillis) {
                it.copy(isNext = true)
            } else {
                it
            }
        }

        val remainingMillis = (nextCandidate.timeMillis - now).coerceAtLeast(0L)
        val hoursRemaining = remainingMillis / (1000 * 60 * 60)
        val minutesRemaining = (remainingMillis / (1000 * 60)) % 60
        val secondsRemaining = (remainingMillis / 1000) % 60
        val countdownString = String.format(Locale.US, "%02d:%02d:%02d", hoursRemaining, minutesRemaining, secondsRemaining)

        val fajrItem = prayerItems.firstOrNull { it.prayer == Prayer.FAJR }
        val maghribItem = prayerItems.firstOrNull { it.prayer == Prayer.MAGHRIB }

        val suhoorEndMillis = fajrItem?.timeMillis ?: now
        val iftarMillis = maghribItem?.timeMillis ?: now

        val dateDisplayFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

        return DailyPrayerSchedule(
            dateString = dateDisplayFormat.format(date),
            hijriDateString = com.example.core.calendar.HijriCalendarHelper.getFormattedHijriDate(date),
            items = updatedItems,
            nextPrayer = nextCandidate,
            remainingMillisToNext = remainingMillis,
            countdownFormatted = countdownString,
            suhoorEndMillis = suhoorEndMillis,
            iftarMillis = iftarMillis,
            formattedSuhoor = fajrItem?.formattedTime ?: "--:--",
            formattedIftar = maghribItem?.formattedTime ?: "--:--"
        )
    }

    private fun computePrayerTimesForDay(
        year: Int, month: Int, day: Int,
        lat: Double, lng: Double,
        tzOffset: Double,
        params: PrayerCalculationParameters
    ): Map<Prayer, Double> {
        val jd = julianDay(year, month, day) - lng / (15.0 * 24.0)

        // Solar coordinates
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(Math.toRadians(g)) + 0.020 * sin(Math.toRadians(2 * g)))
        val e = 23.439 - 0.00000036 * d
        val ra = Math.toDegrees(atan2(cos(Math.toRadians(e)) * sin(Math.toRadians(l)), cos(Math.toRadians(l)))) / 15.0
        val rightAscension = fixHour(ra)

        val declination = Math.toDegrees(asin(sin(Math.toRadians(e)) * sin(Math.toRadians(l))))
        val equationOfTime = (q / 15.0) - rightAscension

        // Midday (Dhuhr)
        val dhuhrBase = 12.0 + tzOffset - (lng / 15.0) - equationOfTime

        // Sunrise & Sunset angle is 0.833 degrees
        val sunAlt = 0.833
        val sunriseHour = dhuhrBase - sunHourAngle(sunAlt, lat, declination)
        val sunsetHour = dhuhrBase + sunHourAngle(sunAlt, lat, declination)

        // Fajr
        val fajrHour = dhuhrBase - sunHourAngle(params.method.fajrAngle, lat, declination)

        // Asr
        val asrShadow = params.madhab.shadowMultiplier.toDouble()
        val asrAlt = -Math.toDegrees(atan(1.0 / (asrShadow + tan(Math.toRadians(abs(lat - declination))))))
        val asrHour = dhuhrBase + sunHourAngle(asrAlt, lat, declination)

        // Maghrib
        val maghribHour = sunsetHour

        // Isha
        val ishaHour = if (params.method.ishaMinutesAfterMaghrib != null) {
            maghribHour + (params.method.ishaMinutesAfterMaghrib / 60.0)
        } else {
            dhuhrBase + sunHourAngle(params.method.ishaAngle, lat, declination)
        }

        return mapOf(
            Prayer.FAJR to fixHour(fajrHour + params.fajrOffsetMinutes / 60.0),
            Prayer.SUNRISE to fixHour(sunriseHour + params.sunriseOffsetMinutes / 60.0),
            Prayer.DHUHR to fixHour(dhuhrBase + params.dhuhrOffsetMinutes / 60.0),
            Prayer.ASR to fixHour(asrHour + params.asrOffsetMinutes / 60.0),
            Prayer.MAGHRIB to fixHour(maghribHour + params.maghribOffsetMinutes / 60.0),
            Prayer.ISHA to fixHour(ishaHour + params.ishaOffsetMinutes / 60.0)
        )
    }

    private fun sunHourAngle(angle: Double, lat: Double, declination: Double): Double {
        val cosH = (-sin(Math.toRadians(angle)) - sin(Math.toRadians(lat)) * sin(Math.toRadians(declination))) /
                (cos(Math.toRadians(lat)) * cos(Math.toRadians(declination)))
        return if (cosH < -1.0) 0.0 else if (cosH > 1.0) 0.0 else Math.toDegrees(acos(cosH)) / 15.0
    }

    private fun julianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun fixAngle(a: Double): Double {
        var angle = a - 360.0 * floor(a / 360.0)
        if (angle < 0) angle += 360.0
        return angle
    }

    private fun fixHour(h: Double): Double {
        var hour = h - 24.0 * floor(h / 24.0)
        if (hour < 0) hour += 24.0
        return hour
    }
}
