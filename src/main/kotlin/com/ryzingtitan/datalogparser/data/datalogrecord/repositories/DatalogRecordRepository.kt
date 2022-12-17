package com.ryzingtitan.datalogparser.data.datalogrecord.repositories

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DatalogRecord
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface DatalogRecordRepository : ReactiveMongoRepository<DatalogRecord, UUID>
