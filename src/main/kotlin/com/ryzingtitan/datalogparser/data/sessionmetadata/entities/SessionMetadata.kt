package com.ryzingtitan.datalogparser.data.sessionmetadata.entities

import lombok.Generated
import java.util.*

@Generated
data class SessionMetadata(
    val sessionId: UUID,
    val startTimeEpochMilliseconds: Long,
    val endTimeEpochMilliseconds: Long,
    val username: String,
)
