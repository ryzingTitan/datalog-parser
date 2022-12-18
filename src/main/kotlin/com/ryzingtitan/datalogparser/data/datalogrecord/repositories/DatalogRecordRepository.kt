package com.ryzingtitan.datalogparser.data.datalogrecord.repositories

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DatalogRecord
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface DatalogRecordRepository : CoroutineCrudRepository<DatalogRecord, UUID>
