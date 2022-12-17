package com.ryzingtitan.datalogparser.presentation

import com.ryzingtitan.datalogparser.domain.parsing.FileParsingService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class ConsoleRunnerTests {
    @BeforeEach
    fun setup() {
        consoleRunner = ConsoleRunner(mockFileParsingService)
    }

    @Nested
    inner class Run {
        @Test
        fun `starts the parsing process`() {
            consoleRunner.run()

            verify(mockFileParsingService, times(1)).parse()
        }
    }

    private lateinit var consoleRunner: ConsoleRunner

    private val mockFileParsingService = mock<FileParsingService>()
}
