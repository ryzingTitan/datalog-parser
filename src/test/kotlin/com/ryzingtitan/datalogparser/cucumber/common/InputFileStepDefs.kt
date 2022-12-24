package com.ryzingtitan.datalogparser.cucumber.common

import com.ryzingtitan.datalogparser.cucumber.repositories.InMemoryInputFileRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given

class InputFileStepDefs {
    @Given("a file with the following rows:")
    fun givenAFileWithTheFollowingData(table: DataTable) {
        InMemoryInputFileRepository.fileLines = mutableListOf()
        createHeaderRow(table)
        createDataRows(table)
    }

    private fun createHeaderRow(table: DataTable) {
        val headerLine = StringBuilder()
        table.row(0).forEach { headerValue ->
            headerLine.append(headerValue).append(',')
        }
        InMemoryInputFileRepository.fileLines.add(headerLine.toString().trimEnd(','))
    }

    private fun createDataRows(table: DataTable) {
        val dataLines = table.tableConverter.toList<String>(table, String::class.java)
        InMemoryInputFileRepository.fileLines.addAll(dataLines)
    }

    @DataTableType
    fun mapFileLine(tableRow: Map<String, String>): String {
        return "${tableRow["Device Time"]}," +
            "${tableRow["Engine Coolant Temperature(°F)"]}," +
            "${tableRow["Engine RPM(rpm)"]}," +
            "${tableRow["Intake Air Temperature(°F)"]}," +
            "${tableRow["Speed (OBD)(mph)"]}," +
            "${tableRow["Throttle Position(Manifold)(%)"]}," +
            "${tableRow["Turbo Boost & Vacuum Gauge(psi)"]}"
    }
}
