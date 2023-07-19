package com.ryzingtitan.datalogparser.data.datalog.repositories

import com.ryzingtitan.datalogparser.data.datalog.entities.Datalog
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DatalogRepository : CoroutineCrudRepository<Datalog, Long>
