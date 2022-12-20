package com.ryzingtitan.datalogparser.presentation

import com.ryzingtitan.datalogparser.domain.parsing.services.FileParsingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!cucumber")
class ConsoleRunner(private val fileParsingService: FileParsingService) : CommandLineRunner {
    private val logger: Logger = LoggerFactory.getLogger(ConsoleRunner::class.java)
    override fun run(vararg args: String?) {
        if (args.size == 1) {
            fileParsingService.parse(args[0]!!)
        } else {
            logger.error("No file was provided to parse")
        }
    }
}
