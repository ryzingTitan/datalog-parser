package com.ryzingtitan.datalogparser.cucumber.common

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given

class InputFileStepDefs {
    @Given("a file with the following rows:")
    fun givenAFileWithTheFollowingData(table: DataTable) {
        fileLines = mutableListOf()
        createHeaderRow(table)
        createDataRows(table)
    }

    private fun createHeaderRow(table: DataTable) {
        val headerLine = StringBuilder()
        table.row(0).forEach { headerValue ->
            headerLine.append(headerValue).append(',')
        }
        fileLines.add(headerLine.toString().trimEnd(','))
    }

    private fun createDataRows(table: DataTable) {
        val dataLine = StringBuilder()
        table.rows(1).values().forEach { value ->
            dataLine.append(value).append(',')
        }

        fileLines.add(dataLine.toString().trimEnd(','))
    }

    companion object InputFileStepDefsData {
        lateinit var fileLines: MutableList<String>
    }
}
