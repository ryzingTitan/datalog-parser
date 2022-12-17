package com.ryzingtitan.datalogparser.domain.parsing

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecordEntity
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DataLogRecordRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class ParsingService(
    private val inputFileRepository: InputFileRepository,
    private val dataLogRecordRepository: DataLogRecordRepository,
    private val uuidGenerator: UuidGenerator
) {
    private val logger: Logger = LoggerFactory.getLogger(ParsingService::class.java)

    fun parse() {
        logger.info("Beginning to parse file")

        val fileLines = inputFileRepository.getInputFileLines()

        val sessionId = uuidGenerator.generate()
        fileLines.drop(1).forEach { fileLine ->
            val dataLogRecord = createDataLogRecord(fileLine, sessionId)

            dataLogRecordRepository.save(dataLogRecord).block()
        }

        logger.info("File parsing completed")
    }

    private fun createDataLogRecord(fileLine: String, sessionId: UUID): DataLogRecordEntity {
        val lineColumns = fileLine.split(',')

        val recordTimestamp = parseFileLineTimestamp(lineColumns[0])
        val intakeAirTemperature = lineColumns[1].toDouble()

        return DataLogRecordEntity(sessionId, recordTimestamp, intakeAirTemperature)
    }

    private fun parseFileLineTimestamp(fileLineTimestamp: String): Instant {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").withZone(ZoneId.of("America/New_York"))
        val parsedDateTime = dateTimeFormatter.parse(fileLineTimestamp)
        return Instant.from(parsedDateTime)
    }
}
