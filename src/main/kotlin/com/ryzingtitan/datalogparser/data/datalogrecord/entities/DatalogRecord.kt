package com.ryzingtitan.datalogparser.data.datalogrecord.entities

import lombok.Generated
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Generated
@Document
data class DatalogRecord(
    @Id val recordId: UUID = UUID.randomUUID(),
    @Indexed val sessionId: UUID,
    @Indexed(direction = IndexDirection.ASCENDING) val timestamp: Instant,
    val longitude: Float,
    val latitude: Float,
    val altitude: Float,
    val intakeAirTemperature: Int?,
    val boostPressure: Float?,
    val coolantTemperature: Int?,
    val engineRpm: Int?,
    val speed: Int?,
    val throttlePosition: Float?
)
