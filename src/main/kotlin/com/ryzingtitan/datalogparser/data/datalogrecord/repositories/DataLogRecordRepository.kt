package com.ryzingtitan.datalogparser.data.datalogrecord.repositories

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecord
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface DataLogRecordRepository : ReactiveMongoRepository<DataLogRecord, UUID>
