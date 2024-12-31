package com.nature.mother.authservice.domain.dto.response.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUserInfo(
    val id: Long,

    @JsonProperty(value = "kakao_account")
    val kakaoAccount: KakaoAccount
)