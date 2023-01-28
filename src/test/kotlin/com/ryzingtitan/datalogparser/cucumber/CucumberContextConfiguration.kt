package com.ryzingtitan.datalogparser.cucumber

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@CucumberContextConfiguration
@ActiveProfiles("cucumber")
@SpringBootTest(
    classes = [com.ryzingtitan.datalogparser.DatalogParserApplication::class],
)
class CucumberContextConfiguration
