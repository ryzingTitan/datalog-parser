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
        val intakeAirTemperature = lineColumns[columnConfiguration.intakeAirTemperature].toDoubleOrNull()
        val boostPressure = lineColumns[columnConfiguration.boostPressure].toDoubleOrNull()

        return DatalogRecord(
            sessionId = sessionId,
            timestamp = recordTimestamp,
            intakeAirTemperature = intakeAirTemperature,
            boostPressure = boostPressure
        )
    }

    private fun parseRowTimestamp(rowTimestamp: String): Instant {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").withZone(ZoneId.of("America/New_York"))
        val parsedDateTime = dateTimeFormatter.parse(rowTimestamp)
        return Instant.from(parsedDateTime)
    }
}
