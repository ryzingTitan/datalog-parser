package com.ryzingtitan.datalogparser.data.datalogrecord.repositories

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DataLogRecordEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface DataLogRecordRepository : MongoRepository<DataLogRecordEntity, UUID>
