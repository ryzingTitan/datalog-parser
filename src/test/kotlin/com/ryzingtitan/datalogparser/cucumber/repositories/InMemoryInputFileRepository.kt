package com.ryzingtitan.datalogparser.cucumber.repositories

import com.ryzingtitan.datalogparser.data.inputfile.repositories.InputFileRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("cucumber")
class InMemoryInputFileRepository : InputFileRepository {
    override fun getInputFileLines(): List<String> {
        return fileLines
    }

    companion object InMemoryInputFileRepositoryData {
        lateinit var fileLines: MutableList<String>
    }
}
