package com.ryzingtitan.datalogparser.data.inputfile.repositories

interface InputFileRepository {
    fun getInputFileLines(filePath: String): List<String>
}
