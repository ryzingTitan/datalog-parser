package com.ryzingtitan.datalogparser.cucumber

import com.ryzingtitan.datalogparser.domain.services.ParsingService
import io.cucumber.java.en.When

class ConsoleRunnerStepDefs(private val parsingService: ParsingService) {
    @When("the file is parsed")
    fun whenTheFileIsParsed() {
        parsingService.parse()
    }
}
