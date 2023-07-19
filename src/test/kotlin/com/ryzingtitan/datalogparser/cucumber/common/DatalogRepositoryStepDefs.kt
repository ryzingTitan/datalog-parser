package com.ryzingtitan.datalogparser.cucumber.common

import com.ryzingtitan.datalogparser.cucumber.components.StaticUuidGenerator.StaticUuidGeneratorSharedState.sessionId
import com.ryzingtitan.datalogparser.data.datalog.entities.Data
import com.ryzingtitan.datalogparser.data.datalog.entities.Datalog
import com.ryzingtitan.datalogparser.data.datalog.entities.TrackInfo
import com.ryzingtitan.datalogparser.data.datalog.entities.User
import com.ryzingtitan.datalogparser.data.datalog.repositories.DatalogRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

class DatalogRepositoryStepDefs(private val datalogRepository: DatalogRepository) {
    @Given("the following datalogs exist:")
    fun givenTheFollowingDatalogsExist(table: DataTable) {
        val existingDatalogs =
            table.tableConverter.toList<Datalog>(table, Datalog::class.java)

        runBlocking {
            existingDatalogs.forEach { existingDatalog ->
                datalogRepository.save(existingDatalog)
            }
        }

        sessionId = UUID.fromString("9628a8bb-0a44-4c31-af7d-a54ff16f080f")
    }

    @Then("the following datalogs will exist:")
    fun thenTheFollowingDatalogsWillExist(table: DataTable) {
        val expectedDatalogs =
            table.tableConverter.toList<Datalog>(table, Datalog::class.java)
                .sortedBy { it.epochMilliseconds }

        val actualDatalogs = mutableListOf<Datalog>()
        runBlocking {
            datalogRepository.findAll().collect { datalog ->
                actualDatalogs.add(datalog)
            }
        }

        assertEquals(
            expectedDatalogs.sortedBy { it.epochMilliseconds },
            actualDatalogs.sortedBy { it.epochMilliseconds },
        )
    }

    @Before
    fun setup() {
        runBlocking {
            datalogRepository.deleteAll()
        }
    }

    @DataTableType
    fun mapDatalog(tableRow: Map<String, String>): Datalog {
        return Datalog(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            epochMilliseconds = tableRow["epochMilliseconds"].toString().toLong(),
            data = Data(
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
            ),
            trackInfo = TrackInfo(
                name = tableRow["trackName"].toString(),
                latitude = tableRow["trackLatitude"].toString().toDouble(),
                longitude = tableRow["trackLongitude"].toString().toDouble(),
            ),
            user = User(
                firstName = tableRow["firstName"].toString(),
                lastName = tableRow["lastName"].toString(),
                email = tableRow["email"].toString(),
            ),
        )
    }
}
