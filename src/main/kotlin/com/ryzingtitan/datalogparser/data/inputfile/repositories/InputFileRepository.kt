package com.ryzingtitan.datalogparser.data.inputfile.repositories

import org.springframework.stereotype.Repository
import java.nio.file.Files
import java.nio.file.Paths

@Repository
class InputFileRepository {
    fun getInputFileLines(filePath: String): List<String> {
        return Files.readAllLines(Paths.get(filePath))
    }
}
