package com.nature.mother.authservice.domain.dto.response.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoProfile(
    val nickname: String?,

    @JsonProperty(value = "profile_image_url")
    val profileImageUrl: String?
)