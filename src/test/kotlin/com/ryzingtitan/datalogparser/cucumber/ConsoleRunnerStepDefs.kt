package com.ryzingtitan.datalogparser.cucumber

import com.ryzingtitan.datalogparser.domain.services.ParsingService
import io.cucumber.java.en.When

class ConsoleRunnerStepDefs(private val parsingService: ParsingService) {
    @When("a user runs the console application")
    fun aUserRunsTheConsoleApplication() {
        parsingService.parse()
    }
}
