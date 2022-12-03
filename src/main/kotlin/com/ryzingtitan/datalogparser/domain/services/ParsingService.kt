package com.ryzingtitan.datalogparser.domain.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ParsingService {
    private val logger: Logger = LoggerFactory.getLogger(ParsingService::class.java)

    fun parse() {
        logger.info("Parsing file")
    }
}
