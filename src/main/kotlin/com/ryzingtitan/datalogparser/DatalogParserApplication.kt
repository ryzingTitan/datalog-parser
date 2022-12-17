package com.ryzingtitan.datalogparser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DatalogParserApplication

fun main(args: Array<String>) {
    runApplication<DatalogParserApplication>(arrayOf(args).toString())
}
