package com.ryzingtitan.datalogparser.presentation

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogparser.domain.parsing.services.FolderParsingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.slf4j.LoggerFactory

class ConsoleRunnerTests {
    @Nested
    inner class Run {
        @Test
        fun `starts the parsing process when a folder is provided`() {
            consoleRunner.run("test")

            verify(mockFolderParsingService, times(1)).parse("test")
        }

        @Test
        fun `logs error message when folder is not provided`() {
            consoleRunner.run()

            verify(mockFolderParsingService, never()).parse(any())

            assertEquals(1, appender.list.size)
            assertEquals(Level.ERROR, appender.list[0].level)
            assertEquals("No folder was provided to parse", appender.list[0].message)
        }
    }

    @BeforeEach
    fun setup() {
        consoleRunner = ConsoleRunner(mockFolderParsingService)

        logger = LoggerFactory.getLogger(ConsoleRunner::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var consoleRunner: ConsoleRunner
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockFolderParsingService = mock<FolderParsingService>()
}
