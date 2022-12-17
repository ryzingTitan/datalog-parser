package com.ryzingtitan.datalogparser.domain.parsing

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecord
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class RowParsingService {
    fun parse(row: String, sessionId: UUID): DataLogRecord {
        return createDataLogRecord(row, sessionId)
    }

    private fun createDataLogRecord(row: String, sessionId: UUID): DataLogRecord {
        val lineColumns = row.split(',')

        val recordTimestamp = parseRowTimestamp(lineColumns[0])
        val intakeAirTemperature = lineColumns[1].toDoubleOrNull()

        return DataLogRecord(
            sessionId = sessionId,
            timestamp = recordTimestamp,
            intakeAirTemperature = intakeAirTemperature
        )
    }

    private fun parseRowTimestamp(rowTimestamp: String): Instant {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").withZone(ZoneId.of("America/New_York"))
        val parsedDateTime = dateTimeFormatter.parse(rowTimestamp)
        return Instant.from(parsedDateTime)
    }
}
