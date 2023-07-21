package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.data.datalog.entities.Data
import com.ryzingtitan.datalogparser.data.datalog.entities.Datalog
import com.ryzingtitan.datalogparser.data.datalog.entities.TrackInfo
import com.ryzingtitan.datalogparser.data.datalog.entities.User
import com.ryzingtitan.datalogparser.domain.parsing.configuration.ColumnConfiguration
import com.ryzingtitan.datalogparser.domain.parsing.configuration.TrackInfoConfiguration
import com.ryzingtitan.datalogparser.domain.parsing.configuration.UserConfiguration
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class RowParsingService(
    private val columnConfiguration: ColumnConfiguration,
    private val userConfiguration: UserConfiguration,
    private val trackInfoConfiguration: TrackInfoConfiguration,
) {
    fun parse(row: String, sessionId: UUID): Datalog {
        return createDatalog(row, sessionId)
    }

    private fun createDatalog(row: String, sessionId: UUID): Datalog {
        val lineColumns = row.split(',')

        val recordTimestamp = parseRowTimestamp(lineColumns[columnConfiguration.deviceTime])

        return Datalog(
            sessionId = sessionId,
            epochMilliseconds = recordTimestamp.toEpochMilli(),
            data = getData(lineColumns),
            trackInfo = getTrackInfo(),
            user = getUser(),
        )
    }

    private fun parseRowTimestamp(rowTimestamp: String): Instant {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").withZone(ZoneId.of("America/New_York"))
        val parsedDateTime = dateTimeFormatter.parse(rowTimestamp)
        return Instant.from(parsedDateTime)
    }

    private fun getData(lineColumns: List<String>): Data {
        val longitude = lineColumns[columnConfiguration.longitude].toDouble()
        val latitude = lineColumns[columnConfiguration.latitude].toDouble()
        val altitude = lineColumns[columnConfiguration.altitude].toFloat()
        val intakeAirTemperature = lineColumns[columnConfiguration.intakeAirTemperature].toFloatOrNull()?.toInt()
        val boostPressure = lineColumns[columnConfiguration.boostPressure].toFloatOrNull()
        val coolantTemperature = lineColumns[columnConfiguration.coolantTemperature].toFloatOrNull()?.toInt()
        val engineRpm = lineColumns[columnConfiguration.engineRpm].toFloatOrNull()?.toInt()
        val speed = lineColumns[columnConfiguration.speed].toFloatOrNull()?.toInt()
        val throttlePosition = lineColumns[columnConfiguration.throttlePosition].toFloatOrNull()
        val airFuelRatio = lineColumns[columnConfiguration.airFuelRatio].toFloatOrNull()

        return Data(
            longitude = longitude,
            latitude = latitude,
            altitude = altitude,
            intakeAirTemperature = intakeAirTemperature,
            boostPressure = boostPressure,
            coolantTemperature = coolantTemperature,
            engineRpm = engineRpm,
            speed = speed,
            throttlePosition = throttlePosition,
            airFuelRatio = airFuelRatio,
        )
    }

    private fun getTrackInfo(): TrackInfo {
        return TrackInfo(
            name = trackInfoConfiguration.name,
            latitude = trackInfoConfiguration.latitude,
            longitude = trackInfoConfiguration.longitude,
        )
    }

    private fun getUser(): User {
        return User(
            email = userConfiguration.email,
            firstName = userConfiguration.firstName,
            lastName = userConfiguration.lastName,
        )
    }
}
