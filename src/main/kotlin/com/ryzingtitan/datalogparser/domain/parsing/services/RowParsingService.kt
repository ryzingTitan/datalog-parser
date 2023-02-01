package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DatalogRecord
import com.ryzingtitan.datalogparser.domain.parsing.configuration.ColumnConfiguration
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class RowParsingService(private val columnConfiguration: ColumnConfiguration) {
    fun parse(row: String, sessionId: UUID): DatalogRecord {
        return createDatalogRecord(row, sessionId)
    }

    private fun createDatalogRecord(row: String, sessionId: UUID): DatalogRecord {
        val lineColumns = row.split(',')

        val recordTimestamp = parseRowTimestamp(lineColumns[columnConfiguration.deviceTime])
        val longitude = lineColumns[columnConfiguration.longitude].toDouble()
        val latitude = lineColumns[columnConfiguration.latitude].toDouble()
        val altitude = lineColumns[columnConfiguration.altitude].toFloat()
        val intakeAirTemperature = lineColumns[columnConfiguration.intakeAirTemperature].toFloatOrNull()?.toInt()
        val boostPressure = lineColumns[columnConfiguration.boostPressure].toFloatOrNull()
        val coolantTemperature = lineColumns[columnConfiguration.coolantTemperature].toFloatOrNull()?.toInt()
        val engineRpm = lineColumns[columnConfiguration.engineRpm].toFloatOrNull()?.toInt()
        val speed = lineColumns[columnConfiguration.speed].toFloatOrNull()?.toInt()
        val throttlePosition = lineColumns[columnConfiguration.throttlePosition].toFloatOrNull()

        return DatalogRecord(
            sessionId = sessionId,
            epochMilliseconds = recordTimestamp.toEpochMilli(),
            longitude = longitude,
            latitude = latitude,
            altitude = altitude,
            intakeAirTemperature = intakeAirTemperature,
            boostPressure = boostPressure,
            coolantTemperature = coolantTemperature,
            engineRpm = engineRpm,
            speed = speed,
            throttlePosition = throttlePosition,
        )
    }

    private fun parseRowTimestamp(rowTimestamp: String): Instant {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").withZone(ZoneId.of("America/New_York"))
        val parsedDateTime = dateTimeFormatter.parse(rowTimestamp)
        return Instant.from(parsedDateTime)
    }
}
