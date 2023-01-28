package com.ryzingtitan.datalogparser.cucumber

import com.ryzingtitan.datalogparser.domain.parsing.services.FolderParsingService
import io.cucumber.java.en.When

class ConsoleRunnerStepDefs(private val folderParsingService: FolderParsingService) {
    @When("the file is parsed")
    fun whenTheFileIsParsed() {
        folderParsingService.parse("./testFiles")
    }
}
