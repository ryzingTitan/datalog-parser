package com.ryzingtitan.datalogparser.cucumber.common

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecord
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DataLogRecordRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.Instant
import java.util.*

class DataLogRecordStepDefs(private val dataLogRecordRepository: DataLogRecordRepository) {
    @Then("the following data log records will be created:")
    fun thenTheFollowingDataLogRecordsWillBeCreated(table: DataTable) {
        val expectedDataLogRecords =
            table.tableConverter.toList<DataLogRecord>(table, DataLogRecord::class.java).sortedBy { it.timestamp }

        val actualDataLogRecords = dataLogRecordRepository.findAll().collectList().block()?.sortedBy { it.timestamp }

        assertEquals(expectedDataLogRecords.count(), actualDataLogRecords?.count())

        for (expectedRecordNumber in 0 until expectedDataLogRecords.count()) {
            assertEquals(
                expectedDataLogRecords[expectedRecordNumber].sessionId,
                actualDataLogRecords!![expectedRecordNumber].sessionId
            )
            assertEquals(
                expectedDataLogRecords[expectedRecordNumber].timestamp,
                actualDataLogRecords[expectedRecordNumber].timestamp
            )
            assertEquals(
                expectedDataLogRecords[expectedRecordNumber].intakeAirTemperature,
                actualDataLogRecords[expectedRecordNumber].intakeAirTemperature
            )
        }
    }

    @Before
    fun setup() {
        dataLogRecordRepository.deleteAll().block()
    }

    @DataTableType
    fun mapDataLogRecord(tableRow: Map<String, String>): DataLogRecord {
        return DataLogRecord(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            timestamp = Instant.parse(tableRow["timestamp"]),
            intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toDoubleOrNull()
        )
    }
}
