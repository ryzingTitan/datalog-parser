package com.ryzingtitan.datalogparser.data.inputfile.repositories

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.nio.file.Files
import java.nio.file.Paths

@Repository
@Profile("production")
class FileSystemInputFileRepository : InputFileRepository {
    override fun getInputFileLines(): List<String> {
        val filePath = Paths.get("C:\\Users\\Kyle\\Downloads\\trackLog-2022-Sep-18_14-15-31.csv")
        return Files.readAllLines(filePath)
    }
}
