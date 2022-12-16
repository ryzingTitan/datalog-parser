package com.ryzingtitan.datalogparser.data.inputfile.repositories

interface InputFileRepository {
    fun getInputFileData(): List<String>
}
