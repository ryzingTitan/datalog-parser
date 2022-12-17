package com.ryzingtitan.datalogparser.domain.uuid

import java.util.UUID

interface UuidGenerator {
    fun generate(): UUID
}
