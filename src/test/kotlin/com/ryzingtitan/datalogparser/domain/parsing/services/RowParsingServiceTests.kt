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
        fun `parses the row correctly when it contains valid session data`() {
            val row = "$firstLineDeviceTime," +
                "${firstLineCoolantTemperature.toFloat()}," +
                "${firstLineEngineRpm.toFloat()}," +
                "${firstLineIntakeAirTemperature.toFloat()}," +
                "${firstLineSpeed.toFloat()}," +
                "$firstLineThrottlePosition," +
                "$firstLineBoostPressure"

            val datalogRecord = rowParsingService.parse(row, sessionId)

            assertNotNull(datalogRecord.recordId)
            assertEquals(sessionId, datalogRecord.sessionId)
            assertEquals(firstLineTimestamp, datalogRecord.timestamp)
            assertEquals(firstLineIntakeAirTemperature, datalogRecord.intakeAirTemperature)
            assertEquals(firstLineBoostPressure, datalogRecord.boostPressure)
            assertEquals(firstLineCoolantTemperature, datalogRecord.coolantTemperature)
            assertEquals(firstLineEngineRpm, datalogRecord.engineRpm)
            assertEquals(firstLineSpeed, datalogRecord.speed)
            assertEquals(firstLineThrottlePosition, datalogRecord.throttlePosition)
        }

        @Test
        fun `parses the row correctly when it contains invalid session data`() {
            val row = "$secondLineDeviceTime," +
                "$secondLineCoolantTemperature," +
                "$secondLineEngineRpm," +
                "$secondLineIntakeAirTemperature," +
                "$secondLineSpeed," +
                "$secondLineThrottlePosition," +
                secondLineBoostPressure

            val datalogRecord = rowParsingService.parse(row, sessionId)

            assertNotNull(datalogRecord.recordId)
            assertEquals(sessionId, datalogRecord.sessionId)
            assertEquals(secondLineTimestamp, datalogRecord.timestamp)
            assertNull(datalogRecord.intakeAirTemperature)
            assertNull(datalogRecord.boostPressure)
            assertNull(datalogRecord.coolantTemperature)
            assertNull(datalogRecord.engineRpm)
            assertNull(datalogRecord.speed)
            assertNull(datalogRecord.throttlePosition)
        }
    }

    @BeforeEach
    fun setup() {
        rowParsingService = RowParsingService(mockColumnConfiguration)

        whenever(mockColumnConfiguration.deviceTime).thenReturn(0)
        whenever(mockColumnConfiguration.coolantTemperature).thenReturn(1)
        whenever(mockColumnConfiguration.engineRpm).thenReturn(2)
        whenever(mockColumnConfiguration.intakeAirTemperature).thenReturn(3)
        whenever(mockColumnConfiguration.speed).thenReturn(4)
        whenever(mockColumnConfiguration.throttlePosition).thenReturn(5)
        whenever(mockColumnConfiguration.boostPressure).thenReturn(6)
    }

    private lateinit var rowParsingService: RowParsingService

    private val mockColumnConfiguration = mock<ColumnConfiguration>()

    companion object RowParsingServiceTestConstants {
        val sessionId: UUID = UUID.fromString("c61cc339-f93d-45a4-aa2b-923f0482b97f")

        const val firstLineDeviceTime = "18-Sep-2022 14:15:47.963"
        const val firstLineIntakeAirTemperature = 123
        const val firstLineBoostPressure = 16.5f
        const val firstLineCoolantTemperature = 155
        const val firstLineEngineRpm = 5500
        const val firstLineSpeed = 86
        const val firstLineThrottlePosition = 95.5f
        val firstLineTimestamp: Instant = Instant.parse("2022-09-18T18:15:47.963Z")

        const val secondLineDeviceTime = "18-Sep-2022 14:18:47.968"
        const val secondLineIntakeAirTemperature = "-"
        const val secondLineBoostPressure = "-"
        const val secondLineCoolantTemperature = "-"
        const val secondLineEngineRpm = "-"
        const val secondLineSpeed = "-"
        const val secondLineThrottlePosition = "-"
        val secondLineTimestamp: Instant = Instant.parse("2022-09-18T18:18:47.968Z")
    }
}
