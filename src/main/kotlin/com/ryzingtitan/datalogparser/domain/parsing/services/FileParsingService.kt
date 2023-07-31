package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class FileParsingService(
    private val inputFileRepository: InputFileRepository,
    private val datalogRepository: DatalogRepository,
    private val uuidGenerator: UuidGenerator,
    private val rowParsingService: RowParsingService,

) {
    private val logger: Logger = LoggerFactory.getLogger(FileParsingService::class.java)

    fun parse(filePath: String) {
        val fileName = filePath.split('\\', '/').last()
        logger.info("Beginning to parse file: $fileName")

        val fileLines = inputFileRepository.getInputFileLines(filePath)
        val fileLinesWithoutHeader = removeHeaderRow(fileLines)

        val sessionId = uuidGenerator.generate()
        fileLinesWithoutHeader.forEach { fileLine ->
            runBlocking {
                val datalog = rowParsingService.parse(fileLine, sessionId)

                if (datalog.sessionId != sessionId) {
                    val oldDatalogs = datalogRepository.deleteBySessionIdAndEpochMilliseconds(
                        datalog.sessionId,
                        datalog.epochMilliseconds,
                    )

                    oldDatalogs.collect()
                }

                datalogRepository.save(datalog)
            }
        }

        logger.info("File parsing completed for file: $fileName")
    }

    private fun removeHeaderRow(fileLines: List<String>): List<String> {
        return fileLines.drop(1)
    }
}
