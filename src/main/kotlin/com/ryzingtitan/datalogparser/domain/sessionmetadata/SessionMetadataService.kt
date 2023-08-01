package com.ryzingtitan.datalogparser.domain.sessionmetadata

import com.ryzingtitan.datalogparser.data.sessionmetadata.repositories.SessionMetadataRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SessionMetadataService(
    private val sessionMetadataRepository: SessionMetadataRepository,
) {
    suspend fun getExistingSessionId(username: String, epochMillisecond: Long): UUID? {
        return sessionMetadataRepository.getAllSessionMetadata()
            .filter { sessionMetadata ->
                sessionMetadata.username == username &&
                    sessionMetadata.startTimeEpochMilliseconds <= epochMillisecond &&
                    sessionMetadata.endTimeEpochMilliseconds >= epochMillisecond
            }.firstOrNull()?.sessionId
    }
}
