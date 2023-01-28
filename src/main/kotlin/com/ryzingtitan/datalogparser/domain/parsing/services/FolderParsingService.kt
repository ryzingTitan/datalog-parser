package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.data.inputfolder.repositories.InputFolderRepository
import org.springframework.stereotype.Service

@Service
class FolderParsingService(
    private val inputFolderRepository: InputFolderRepository,
    private val fileParsingService: FileParsingService,
) {
    fun parse(folderPath: String) {
        val filePaths = inputFolderRepository.getInputFiles(folderPath)
        filePaths.forEach { filePath ->
            fileParsingService.parse(filePath)
        }
    }
}
