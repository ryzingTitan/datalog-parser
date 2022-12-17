package com.ryzingtitan.datalogparser.presentation

import com.ryzingtitan.datalogparser.domain.parsing.services.FileParsingService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ConsoleRunnerTests {
    @Nested
    inner class Run {
        @Test
        fun `starts the parsing process`() {
            consoleRunner.run()

            verify(mockFileParsingService, times(1)).parse()
        }
    }

    @BeforeEach
    fun setup() {
        consoleRunner = ConsoleRunner(mockFileParsingService)
    }

    private lateinit var consoleRunner: ConsoleRunner

    private val mockFileParsingService = mock<FileParsingService>()
}
