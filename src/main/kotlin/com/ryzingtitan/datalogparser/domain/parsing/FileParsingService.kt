package com.ryzingtitan.datalogparser.domain.parsing

import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DataLogRecordRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FileParsingService(
    private val inputFileRepository: InputFileRepository,
    private val dataLogRecordRepository: DataLogRecordRepository,
    private val uuidGenerator: UuidGenerator,
    private val rowParsingService: RowParsingService
) {
    private val logger: Logger = LoggerFactory.getLogger(FileParsingService::class.java)

    fun parse() {
        logger.info("Beginning to parse file")

        val fileLines = inputFileRepository.getInputFileLines()
        val fileLinesWithoutHeader = removeHeaderRow(fileLines)

        val sessionId = uuidGenerator.generate()
        fileLinesWithoutHeader.forEach { fileLine ->
            val dataLogRecord = rowParsingService.parse(fileLine, sessionId)

            dataLogRecordRepository.save(dataLogRecord).block()
        }

        logger.info("File parsing completed")
    }

    fun removeHeaderRow(fileLines: List<String>): List<String> {
        return fileLines.drop(1)
    }
}
