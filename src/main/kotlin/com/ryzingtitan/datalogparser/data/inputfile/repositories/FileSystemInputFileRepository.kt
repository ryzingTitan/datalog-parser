package com.ryzingtitan.datalogparser.data.inputfile.repositories

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("production")
class FileSystemInputFileRepository : InputFileRepository {
    override fun getInputFileLines(): List<String> {
        return emptyList()
    }
}
