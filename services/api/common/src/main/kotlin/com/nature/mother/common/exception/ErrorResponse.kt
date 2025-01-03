package com.nature.mother.common.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val statusCode: String,
    val time: LocalDateTime = LocalDateTime.now()
)