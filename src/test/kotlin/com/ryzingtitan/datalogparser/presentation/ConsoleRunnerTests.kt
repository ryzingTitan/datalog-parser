package com.ryzingtitan.datalogparser.presentation

import com.ryzingtitan.datalogparser.domain.services.ParsingService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ConsoleRunnerTests {
    @BeforeEach
    fun setup() {
        consoleRunner = ConsoleRunner(mockParsingService)
    }

    @Nested
    inner class Run {
        @Test
        fun `runs the report`() {
            consoleRunner.run()

            verify(mockParsingService, times(1)).parse()
        }
    }

    private lateinit var consoleRunner: ConsoleRunner

    private val mockParsingService = mock<ParsingService>()
}
