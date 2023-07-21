package com.ryzingtitan.datalogparser.domain.parsing.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogparser.data.datalog.entities.Data
import com.ryzingtitan.datalogparser.data.datalog.entities.Datalog
import com.ryzingtitan.datalogparser.data.datalog.entities.TrackInfo
import com.ryzingtitan.datalogparser.data.datalog.entities.User
import com.ryzingtitan.datalogparser.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
import java.time.Instant
import java.util.*

@ExperimentalCoroutinesApi
class FileParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `reads the input data and creates data log records`() = runTest {
            whenever(mockDatalogRepository.save(datalog)).thenReturn(datalog)

            fileParsingService.parse("testFile")

            verify(mockInputFileRepository, times(1)).getInputFileLines("testFile")
            verify(mockRowParsingService, times(1)).parse("data row 1", sessionId)
            verify(mockDatalogRepository, times(1)).save(any())

            assertEquals(2, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Beginning to parse file: testFile", appender.list[0].message)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("File parsing completed for file: testFile", appender.list[1].message)
        }
    }

    @BeforeEach
    fun setup() {
        fileParsingService = FileParsingService(
            mockInputFileRepository,
            mockDatalogRepository,
            mockUuidGenerator,
            mockRowParsingService,
        )

        whenever(mockInputFileRepository.getInputFileLines("testFile")).thenReturn(listOf("header row", "data row 1"))
        whenever(mockUuidGenerator.generate()).thenReturn(sessionId)
        whenever(mockRowParsingService.parse("data row 1", sessionId)).thenReturn(datalog)

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
    private val mockDatalogRepository = mock<DatalogRepository>()
    private val mockUuidGenerator = mock<UuidGenerator>()
    private val mockRowParsingService = mock<RowParsingService>()

    private val sessionId = UUID.randomUUID()
    private val datalog = Datalog(
        sessionId = sessionId,
        epochMilliseconds = Instant.now().toEpochMilli(),
        data = Data(
            longitude = -86.14162,
            latitude = 42.406800000000004,
            altitude = 188.4f,
            intakeAirTemperature = 138,
            boostPressure = 16.5f,
            coolantTemperature = 155,
            engineRpm = 3500,
            speed = 79,
            throttlePosition = 83.2f,
            airFuelRatio = 17.5f,
        ),
        trackInfo = TrackInfo(
            name = "Test Track",
            latitude = 42.4086,
            longitude = -86.1374,
        ),
        user = User(
            email = "test@test.com",
            firstName = "test",
            lastName = "tester",
        ),
    )
}
