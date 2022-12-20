package com.ryzingtitan.datalogparser

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
class DatalogParserApplicationTests {
    @Nested
    inner class Context {
        @Test
        fun `loads successfully`() {
            // Context test
        }
    }
}
