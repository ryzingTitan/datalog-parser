package com.ryzingtitan.datalogparser.data.datalogrecord.entities

import lombok.Generated
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Generated
@Document
data class DatalogRecord(
    @Id val recordId: UUID = UUID.randomUUID(),
    val sessionId: UUID,
    val timestamp: Instant,
    val intakeAirTemperature: Double?,
    val boostPressure: Double?,
    val coolantTemperature: Double?,
    val engineRpm: Int?,
    val speed: Int?
)
