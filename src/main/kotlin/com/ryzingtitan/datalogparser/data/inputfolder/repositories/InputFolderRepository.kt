package com.ryzingtitan.datalogparser.data.inputfolder.repositories

import org.springframework.stereotype.Repository
import java.nio.file.Files
import java.nio.file.Paths

@Repository
class InputFolderRepository {
    fun getInputFiles(folderPath: String): List<String> {
        val inputFilePaths = Files.list(Paths.get(folderPath))

        val inputFiles = mutableListOf<String>()
        inputFilePaths.forEach { inputFilePath ->
            inputFiles.add(inputFilePath.toString())
        }

        return inputFiles
    }
}
