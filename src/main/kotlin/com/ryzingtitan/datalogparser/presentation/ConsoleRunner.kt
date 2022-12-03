package com.ryzingtitan.datalogparser.presentation

import com.ryzingtitan.datalogparser.domain.services.ParsingService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!cucumber-test")
class ConsoleRunner(private val parsingService: ParsingService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        parsingService.parse()
    }
}
