package com.ryzingtitan.datalogparser.data.datalog.entities

import lombok.Generated
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Generated
@Document
data class Datalog(
    @Indexed val sessionId: UUID,
    @Id val epochMilliseconds: Long,
    val data: Data,
    val trackInfo: TrackInfo,
    val user: User,
)
