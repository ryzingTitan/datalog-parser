package com.ryzingtitan.datalogparser.domain.parsing.configuration

import lombok.Generated
import org.springframework.boot.context.properties.ConfigurationProperties

@Generated
@ConfigurationProperties(prefix = "track-info")
data class TrackInfoConfiguration(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)
