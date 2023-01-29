package com.ryzingtitan.datalogparser.cucumber.components

import com.ryzingtitan.datalogparser.domain.uuid.UuidGenerator
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("cucumber")
class StaticUuidGenerator : UuidGenerator {
    override fun generate(): UUID {
        return sessionId
    }

    companion object StaticUuidGeneratorSharedState {
        internal var sessionId = UUID.fromString("c61cc339-f93d-45a4-aa2b-923f0482b97f")
    }
}
