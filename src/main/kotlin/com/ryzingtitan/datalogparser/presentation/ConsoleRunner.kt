package com.ryzingtitan.datalogparser.presentation

import com.ryzingtitan.datalogparser.domain.parsing.services.FolderParsingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!cucumber")
class ConsoleRunner(private val folderParsingService: FolderParsingService) : CommandLineRunner {
    private val logger: Logger = LoggerFactory.getLogger(ConsoleRunner::class.java)
    override fun run(vararg args: String?) {
        if (args.size == 1) {
            folderParsingService.parse(args[0]!!)
        } else {
            logger.error("No folder was provided to parse")
        }
    }
}
