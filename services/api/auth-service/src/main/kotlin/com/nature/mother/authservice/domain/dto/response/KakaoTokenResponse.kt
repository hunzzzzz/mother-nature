package com.nature.mother.authservice.domain.dto.response

import com.nature.mother.authservice.domain.dto.response.kakao.KakaoToken

data class KakaoTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val kakaoToken: KakaoToken
) {
    companion object {
        fun from(accessToken: String, refreshToken: String, kakaoToken: KakaoToken) = KakaoTokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            kakaoToken = kakaoToken
        )
    }
}
