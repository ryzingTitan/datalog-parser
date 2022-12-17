package com.ryzingtitan.datalogparser.domain.parsing

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecord
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DataLogRecordRepository
import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import java.util.UUID

@ExperimentalCoroutinesApi
class ParsingServiceTests {
    @BeforeEach
    fun setup() {
        parsingService = ParsingService(
            mockInputFileRepository,
            mockDataLogRecordRepository,
            mockUuidGenerator
        )

        whenever(mockInputFileRepository.getInputFileLines())
            .thenReturn(
                listOf(
                    "Device Time, Intake Air Temperature",
                    "$firstLineDeviceTime,$firstLineIntakeAirTemperature",
                    "$secondLineDeviceTime,$secondLineIntakeAirTemperature",
                    "$thirdLineDeviceTime,$thirdLineIntakeAirTemperature"
                )
            )

        whenever(mockUuidGenerator.generate()).thenReturn(sessionId)
        whenever(mockDataLogRecordRepository.save(any()))
            .thenReturn(Mono.just(firstLineDataLogRecord))
            .thenReturn(Mono.just(secondLineDataLogRecord))
            .thenReturn(Mono.just(thirdLineDataLogRecord))

        logger = LoggerFactory.getLogger(ParsingService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    @Nested
    inner class Parse {
        @Test
        fun `reads the input data and creates data log records`() {
            parsingService.parse()

            verify(mockInputFileRepository, times(1)).getInputFileLines()
            verify(mockDataLogRecordRepository, times(3)).save(any())
//            verify(mockDataLogRecordRepository, times(1)).save(firstLineDataLogRecord)
//            verify(mockDataLogRecordRepository, times(1)).save(secondLineDataLogRecord)
//            verify(mockDataLogRecordRepository, times(1)).save(thirdLineDataLogRecord)

            assertEquals(2, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Beginning to parse file", appender.list[0].message)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("File parsing completed", appender.list[1].message)
        }
    }

    private lateinit var parsingService: ParsingService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockInputFileRepository = mock<InputFileRepository>()
    private val mockDataLogRecordRepository = mock<DataLogRecordRepository>()
    private val mockUuidGenerator = mock<UuidGenerator>()

    private val firstLineDataLogRecord = DataLogRecord(
        sessionId = sessionId,
        timestamp = firstLineTimestamp,
        intakeAirTemperature = firstLineIntakeAirTemperature
    )

    private val secondLineDataLogRecord = DataLogRecord(
        sessionId = sessionId,
        timestamp = secondLineTimestamp,
        intakeAirTemperature = secondLineIntakeAirTemperature.toDouble()
    )

    private val thirdLineDataLogRecord = DataLogRecord(
        sessionId = sessionId,
        timestamp = thirdLineTimestamp,
        intakeAirTemperature = null
    )

    companion object ParsingServiceTestConstants {
        val sessionId: UUID = UUID.fromString("c61cc339-f93d-45a4-aa2b-923f0482b97f")

        const val firstLineDeviceTime = "18-Sep-2022 14:15:47.963"
        const val firstLineIntakeAirTemperature = 123.8
        val firstLineTimestamp: Instant = Instant.parse("2022-09-18T18:15:47.963Z")

        const val secondLineDeviceTime = "18-Sep-2022 14:18:47.968"
        const val secondLineIntakeAirTemperature = 130
        val secondLineTimestamp: Instant = Instant.parse("2022-09-18T18:18:47.968Z")

        const val thirdLineDeviceTime = "18-Sep-2022 14:18:48.968"
        const val thirdLineIntakeAirTemperature = "-"
        val thirdLineTimestamp: Instant = Instant.parse("2022-09-18T18:18:48.968Z")
    }
}
