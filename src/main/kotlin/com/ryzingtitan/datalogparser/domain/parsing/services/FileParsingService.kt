package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import kotlinx.coroutines.runBlocking
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

    fun parse(filePath: String) {
        logger.info("Beginning to parse file: $filePath")

        val fileLines = inputFileRepository.getInputFileLines(filePath)
        val fileLinesWithoutHeader = removeHeaderRow(fileLines)

        val sessionId = uuidGenerator.generate()
        fileLinesWithoutHeader.forEach { fileLine ->
            val datalogRecord = rowParsingService.parse(fileLine, sessionId)

            runBlocking {
                datalogRecordRepository.save(datalogRecord)
            }
        }

        logger.info("File parsing completed for file: $filePath")
    }

    fun removeHeaderRow(fileLines: List<String>): List<String> {
        return fileLines.drop(1)
    }
}
