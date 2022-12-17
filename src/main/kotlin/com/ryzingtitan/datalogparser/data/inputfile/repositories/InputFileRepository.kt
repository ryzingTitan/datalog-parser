package com.ryzingtitan.datalogparser.data.inputfile.repositories

interface InputFileRepository {
    fun getInputFileLines(): List<String>
}
