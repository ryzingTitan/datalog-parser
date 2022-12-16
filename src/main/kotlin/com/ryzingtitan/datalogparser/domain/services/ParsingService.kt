package com.ryzingtitan.datalogparser.domain.services

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecordEntity
import com.ryzingtitan.datalogparser.data.datalogrecord.repositories.DataLogRecordRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ParsingService(private val dataLogRecordRepository: DataLogRecordRepository) {
    private val logger: Logger = LoggerFactory.getLogger(ParsingService::class.java)

    fun parse() {
        logger.info("Parsing file")
        val id = UUID.randomUUID()
        val dataLogRecord = DataLogRecordEntity(id, Instant.now(), 120.0)
        dataLogRecordRepository.save(dataLogRecord)

        val result = dataLogRecordRepository.findById(id)
        logger.info(result.toString())
    }
}
