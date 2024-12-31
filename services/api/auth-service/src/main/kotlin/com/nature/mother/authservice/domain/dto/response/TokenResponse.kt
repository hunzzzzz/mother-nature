package com.nature.mother.authservice.domain.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)