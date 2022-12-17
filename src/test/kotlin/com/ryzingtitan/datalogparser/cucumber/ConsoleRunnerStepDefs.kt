package com.ryzingtitan.datalogparser.cucumber

import com.ryzingtitan.datalogparser.domain.parsing.FileParsingService
import io.cucumber.java.en.When

class ConsoleRunnerStepDefs(private val fileParsingService: FileParsingService) {
    @When("the file is parsed")
    fun whenTheFileIsParsed() {
        fileParsingService.parse()
    }
}
