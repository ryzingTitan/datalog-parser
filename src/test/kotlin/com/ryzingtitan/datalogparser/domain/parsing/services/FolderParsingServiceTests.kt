package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.data.inputfolder.repositories.InputFolderRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FolderParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `parses each file inside the folder`() {
            whenever(mockInputFolderRepository.getInputFiles("testFolder"))
                .thenReturn(listOf("testFolder/testFile1.txt", "testFolder/testFile2.txt"))

            folderParsingService.parse("testFolder")

            verify(mockInputFolderRepository, times(1)).getInputFiles("testFolder")
            verify(mockFileParsingService, times(1)).parse("testFolder/testFile1.txt")
            verify(mockFileParsingService, times(1)).parse("testFolder/testFile2.txt")
        }
    }

    @BeforeEach
    fun setup() {
        folderParsingService = FolderParsingService(
            mockInputFolderRepository,
            mockFileParsingService,
        )
    }

    private lateinit var folderParsingService: FolderParsingService

    private val mockInputFolderRepository = mock<InputFolderRepository>()
    private val mockFileParsingService = mock<FileParsingService>()
}
