package com.ryzingtitan.datalogparser.cucumber.common

import com.ryzingtitan.datalogparser.cucumber.components.StaticUuidGenerator.StaticUuidGeneratorSharedState.sessionId
import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DatalogRecord
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DatalogRecordRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

class DatalogRecordRepositoryStepDefs(private val datalogRecordRepository: DatalogRecordRepository) {
    @Given("the following datalog records exist:")
    fun givenTheFollowingDatalogRecordsExist(table: DataTable) {
        val existingDatalogRecords =
            table.tableConverter.toList<DatalogRecord>(table, DatalogRecord::class.java)

        runBlocking {
            existingDatalogRecords.forEach { existingDatalogRecord ->
                datalogRecordRepository.save(existingDatalogRecord)
            }
        }

        sessionId = UUID.fromString("9628a8bb-0a44-4c31-af7d-a54ff16f080f")
    }

    @Then("the following datalog records will exist:")
    fun thenTheFollowingDatalogRecordsWillExist(table: DataTable) {
        val expectedDatalogRecords =
            table.tableConverter.toList<DatalogRecord>(table, DatalogRecord::class.java)
                .sortedBy { it.epochMilliseconds }

        val actualDatalogRecords = mutableListOf<DatalogRecord>()
        runBlocking {
            datalogRecordRepository.findAll().collect { datalogRecord ->
                actualDatalogRecords.add(datalogRecord)
            }
        }

        assertEquals(
            expectedDatalogRecords.sortedBy { it.epochMilliseconds },
            actualDatalogRecords.sortedBy { it.epochMilliseconds },
        )
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
            epochMilliseconds = tableRow["epochMilliseconds"].toString().toLong(),
            longitude = tableRow["longitude"].toString().toDouble(),
            latitude = tableRow["latitude"].toString().toDouble(),
            altitude = tableRow["altitude"].toString().toFloat(),
            intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toIntOrNull(),
            boostPressure = tableRow["boostPressure"].toString().toFloatOrNull(),
            coolantTemperature = tableRow["coolantTemperature"].toString().toIntOrNull(),
            engineRpm = tableRow["engineRpm"].toString().toIntOrNull(),
            speed = tableRow["speed"].toString().toIntOrNull(),
            throttlePosition = tableRow["throttlePosition"].toString().toFloatOrNull(),
            airFuelRatio = tableRow["airFuelRatio"].toString().toFloatOrNull(),
        )
    }
}
