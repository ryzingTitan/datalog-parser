package com.ryzingtitan.datalogparser.presentation

import com.ryzingtitan.datalogparser.domain.parsing.FileParsingService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!cucumber")
class ConsoleRunner(private val fileParsingService: FileParsingService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        fileParsingService.parse()
    }
}
