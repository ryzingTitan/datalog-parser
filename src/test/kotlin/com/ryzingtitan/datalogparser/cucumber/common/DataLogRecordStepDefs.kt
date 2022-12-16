package com.ryzingtitan.datalogparser.cucumber.common

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecordEntity
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DataLogRecordRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.Instant
import java.util.*

class DataLogRecordStepDefs(private val dataLogRecordRepository: DataLogRecordRepository) {
    @Then("the following data log records will be created:")
    fun thenTheFollowingDataLogRecordsWillBeCreated(table: DataTable) {
        val expectedDataLogRecords = table.tableConverter.toList<DataLogRecordEntity>(table, DataLogRecordEntity::class.java)

        val actualDataLogRecords = dataLogRecordRepository.findAll()

        assertEquals(expectedDataLogRecords.sortedBy { it.timestamp }, actualDataLogRecords.sortedBy { it.timestamp })
    }

    @DataTableType
    fun mapDataLogRecord(tableRow: Map<String, String>): DataLogRecordEntity {
        return DataLogRecordEntity(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            timestamp = Instant.parse(tableRow["timestamp"]),
            intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toDouble()
        )
    }
}
