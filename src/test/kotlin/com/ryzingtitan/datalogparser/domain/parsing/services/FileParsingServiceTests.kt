package com.ryzingtitan.datalogparser.domain.parsing.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DatalogRecord
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*

class FileParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `reads the input data and creates data log records`() {
            fileParsingService.parse()

            verify(mockInputFileRepository, times(1)).getInputFileLines()
            verify(mockRowParsingService, times(1)).parse("data row 1", sessionId)
            verify(mockDatalogRecordRepository, times(1)).save(any())

            assertEquals(2, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Beginning to parse file", appender.list[0].message)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("File parsing completed", appender.list[1].message)
        }
    }

    @BeforeEach
    fun setup() {
        fileParsingService = FileParsingService(
            mockInputFileRepository,
            mockDatalogRecordRepository,
            mockUuidGenerator,
            mockRowParsingService
        )

        whenever(mockInputFileRepository.getInputFileLines()).thenReturn(listOf("header row", "data row 1"))
        whenever(mockUuidGenerator.generate()).thenReturn(sessionId)
        whenever(mockRowParsingService.parse("data row 1", sessionId)).thenReturn(datalogRecord)
        whenever(mockDatalogRecordRepository.save(datalogRecord))
            .thenReturn(Mono.just(datalogRecord))

        logger = LoggerFactory.getLogger(FileParsingService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var fileParsingService: FileParsingService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockInputFileRepository = mock<InputFileRepository>()
    private val mockDatalogRecordRepository = mock<DatalogRecordRepository>()
    private val mockUuidGenerator = mock<UuidGenerator>()
    private val mockRowParsingService = mock<RowParsingService>()

    private val sessionId = UUID.randomUUID()
    private val datalogRecord = DatalogRecord(
        sessionId = sessionId,
        timestamp = Instant.now(),
        intakeAirTemperature = 138.5
    )
}
