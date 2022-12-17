package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.domain.parsing.configuration.ColumnConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.*

class RowParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `parses the row correctly when it contains a valid intake air temperature`() {
            val row = "$firstLineDeviceTime,$firstLineIntakeAirTemperature"

            val datalogRecord = rowParsingService.parse(row, sessionId)

            assertNotNull(datalogRecord.recordId)
            assertEquals(sessionId, datalogRecord.sessionId)
            assertEquals(firstLineTimestamp, datalogRecord.timestamp)
            assertEquals(firstLineIntakeAirTemperature, datalogRecord.intakeAirTemperature)
        }

        @Test
        fun `parses the row correctly when it contains an invalid intake air temperature`() {
            val row = "$secondLineDeviceTime,$secondLineIntakeAirTemperature"

            val datalogRecord = rowParsingService.parse(row, sessionId)

            assertNotNull(datalogRecord.recordId)
            assertEquals(sessionId, datalogRecord.sessionId)
            assertEquals(secondLineTimestamp, datalogRecord.timestamp)
            assertNull(datalogRecord.intakeAirTemperature)
        }
    }

    @BeforeEach
    fun setup() {
        rowParsingService = RowParsingService(mockColumnConfiguration)

        whenever(mockColumnConfiguration.deviceTime).thenReturn(0)
        whenever(mockColumnConfiguration.intakeAirTemperature).thenReturn(1)
    }

    private lateinit var rowParsingService: RowParsingService

    private val mockColumnConfiguration = mock<ColumnConfiguration>()

    companion object RowParsingServiceTestConstants {
        val sessionId: UUID = UUID.fromString("c61cc339-f93d-45a4-aa2b-923f0482b97f")

        const val firstLineDeviceTime = "18-Sep-2022 14:15:47.963"
        const val firstLineIntakeAirTemperature = 123.8
        val firstLineTimestamp: Instant = Instant.parse("2022-09-18T18:15:47.963Z")

        const val secondLineDeviceTime = "18-Sep-2022 14:18:47.968"
        const val secondLineIntakeAirTemperature = "-"
        val secondLineTimestamp: Instant = Instant.parse("2022-09-18T18:18:47.968Z")
    }
}
