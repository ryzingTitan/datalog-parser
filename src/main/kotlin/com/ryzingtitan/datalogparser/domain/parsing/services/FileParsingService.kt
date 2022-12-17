package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FileParsingService(
    private val inputFileRepository: InputFileRepository,
    private val datalogRecordRepository: DatalogRecordRepository,
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
            val datalogRecord = rowParsingService.parse(fileLine, sessionId)

            datalogRecordRepository.save(datalogRecord).block()
        }

        logger.info("File parsing completed")
    }

    fun removeHeaderRow(fileLines: List<String>): List<String> {
        return fileLines.drop(1)
    }
}
