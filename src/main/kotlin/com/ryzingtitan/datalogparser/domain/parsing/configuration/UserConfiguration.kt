package com.ryzingtitan.datalogparser.domain.parsing.configuration

import lombok.Generated
import org.springframework.boot.context.properties.ConfigurationProperties

@Generated
@ConfigurationProperties(prefix = "user")
data class UserConfiguration(
    val firstName: String,
    val lastName: String,
    val email: String,
)
