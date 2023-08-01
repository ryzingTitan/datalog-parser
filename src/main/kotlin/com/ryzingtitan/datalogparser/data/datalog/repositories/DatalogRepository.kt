package com.ryzingtitan.datalogparser.data.datalog.repositories

import com.ryzingtitan.datalogparser.data.datalog.entities.Datalog
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface DatalogRepository : CoroutineCrudRepository<Datalog, String> {
    fun deleteBySessionIdAndEpochMilliseconds(sessionId: UUID, epochMilliseconds: Long): Flow<Datalog>
}
