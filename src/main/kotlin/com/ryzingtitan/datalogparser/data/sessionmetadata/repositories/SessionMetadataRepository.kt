package com.ryzingtitan.datalogparser.data.sessionmetadata.repositories

import com.ryzingtitan.datalogparser.data.datalog.entities.Datalog
import com.ryzingtitan.datalogparser.data.sessionmetadata.entities.SessionMetadata
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SessionMetadataRepository : CoroutineCrudRepository<Datalog, UUID> {
    @Aggregation(
        """
            { '${'$'}group': 
                { '_id': '${'$'}sessionId', 
                  'sessionId': { '${'$'}first': '${'$'}sessionId' }, 
                  'username': { '${'$'}first': '${'$'}user.email' }, 
                  'startTimeEpochMilliseconds': { '${'$'}min': '${'$'}epochMilliseconds' }, 
                  'endTimeEpochMilliseconds': { '${'$'}max': '${'$'}epochMilliseconds'}
                }
            }
          """,
    )
    fun getAllSessionMetadata(): Flow<SessionMetadata>
}
