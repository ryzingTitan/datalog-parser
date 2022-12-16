package com.ryzingtitan.datalogparser.cucumber.common

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogparser.cucumber.dtos.LogMessage
import com.ryzingtitan.datalogparser.domain.services.ParsingService
import io.cucumber.datatable.DataTable
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import org.slf4j.LoggerFactory

class LoggingStepDefs {
    @Then("the application will log the following messages:")
    fun thenTheApplicationWilLogTheFollowingMessages(table: DataTable) {
        val expectedLogMessages: List<LogMessage> = table.tableConverter.toList(table, LogMessage::class.java)

        val actualLogMessages = mutableListOf<LogMessage>()

        appender.list.forEach {
            actualLogMessages.add(LogMessage(it.level.levelStr, it.message))
        }

        assertEquals(expectedLogMessages.sortedBy { it.message }, actualLogMessages.sortedBy { it.message })
    }

    @Before
    fun setup() {
        consoleLogger = LoggerFactory.getLogger(ParsingService::class.java) as Logger
        consoleLogger.addAppender(appender)

        appender.start()
    }

    @After
    fun teardown() {
        appender.stop()
    }

    @DataTableType
    fun mapLogMessage(tableRow: Map<String, String>): LogMessage {
        return LogMessage(
            level = tableRow["level"].toString(),
            message = tableRow["message"].toString()
        )
    }

    private lateinit var consoleLogger: Logger

    private val appender: ListAppender<ILoggingEvent> = ListAppender()
}
