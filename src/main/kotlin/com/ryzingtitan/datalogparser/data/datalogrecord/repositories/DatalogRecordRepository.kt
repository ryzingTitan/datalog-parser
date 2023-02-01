package com.ryzingtitan.datalogparser.data.datalogrecord.repositories

import com.ryzingtitan.datalogparser.data.datalogrecord.entities.DatalogRecord
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DatalogRecordRepository : CoroutineCrudRepository<DatalogRecord, Long>
