package com.ryzingtitan.datalogparser.domain.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DataLogRecordRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.slf4j.LoggerFactory

@ExperimentalCoroutinesApi
class ParsingServiceTests {
    @BeforeEach
    fun setup() {
        parsingService = ParsingService(mockDataLogRecordRepository)

        logger = LoggerFactory.getLogger(ParsingService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    @Nested
    inner class Parse {
        @Test
        fun `returns correct data from the job`() = runTest {
            parsingService.parse()

            assertEquals(1, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Parsing file", appender.list[0].message)
        }
    }

    private lateinit var parsingService: ParsingService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockDataLogRecordRepository = mock<DataLogRecordRepository>()
}
