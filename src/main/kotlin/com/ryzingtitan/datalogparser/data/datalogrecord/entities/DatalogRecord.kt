package com.ryzingtitan.datalogparser.data.datalogrecord.entities

import lombok.Generated
import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.UUID

@Generated
data class DatalogRecord(
    @Id val recordId: UUID = UUID.randomUUID(),
    val sessionId: UUID,
    val timestamp: Instant,
    val intakeAirTemperature: Double?
)
