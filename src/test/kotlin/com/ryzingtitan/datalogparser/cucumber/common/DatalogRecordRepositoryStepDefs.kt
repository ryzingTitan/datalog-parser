package com.ryzingtitan.datalogparser.cucumber.common

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DatalogRecord
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DatalogRecordRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Then
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.Instant
import java.util.*

class DatalogRecordRepositoryStepDefs(private val datalogRecordRepository: DatalogRecordRepository) {
    @Then("the following data log records will be created:")
    fun thenTheFollowingDatalogRecordsWillBeCreated(table: DataTable) {
        val expectedDatalogRecords =
            table.tableConverter.toList<DatalogRecord>(table, DatalogRecord::class.java).sortedBy { it.timestamp }

        val actualDatalogRecords = mutableListOf <DatalogRecord>()
        runBlocking {
            datalogRecordRepository.findAll().collect { datalogRecord ->
                actualDatalogRecords.add(datalogRecord)
            }
        }

        assertEquals(expectedDatalogRecords.count(), actualDatalogRecords.count())

        for (expectedRecordNumber in 0 until expectedDatalogRecords.count()) {
            assertEquals(
                expectedDatalogRecords[expectedRecordNumber].sessionId,
                actualDatalogRecords[expectedRecordNumber].sessionId
            )
            assertEquals(
                expectedDatalogRecords[expectedRecordNumber].timestamp,
                actualDatalogRecords[expectedRecordNumber].timestamp
            )
            assertEquals(
                expectedDatalogRecords[expectedRecordNumber].intakeAirTemperature,
                actualDatalogRecords[expectedRecordNumber].intakeAirTemperature
            )
        }
    }

    @Before
    fun setup() {
        runBlocking {
            datalogRecordRepository.deleteAll()
        }
    }

    @DataTableType
    fun mapDatalogRecord(tableRow: Map<String, String>): DatalogRecord {
        return DatalogRecord(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            timestamp = Instant.parse(tableRow["timestamp"]),
            intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toDoubleOrNull(),
            boostPressure = tableRow["boostPressure"].toString().toDoubleOrNull(),
            coolantTemperature = tableRow["coolantTemperature"].toString().toDoubleOrNull(),
            engineRpm = tableRow["engineRpm"].toString().toIntOrNull(),
            speed = tableRow["speed"].toString().toIntOrNull()
        )
    }
}
