package com.ryzingtitan.datalogparser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableReactiveMongoRepositories
class DatalogParserApplication

fun main(args: Array<String>) {
    runApplication<DatalogParserApplication>(arrayOf(args).toString())
}
