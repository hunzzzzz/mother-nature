package com.nature.mother.userservice.domain.user.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.nature.mother.common.dto.KakaoSignup
import com.nature.mother.common.model.UserType
import com.nature.mother.userservice.domain.user.model.KakaoUser
import com.nature.mother.userservice.domain.user.repository.KakaoUserRepository
import com.nature.mother.userservice.global.utility.NicknameGenerator.generateNickname
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuthSignUpService(
    private val kakaoUserRepository: KakaoUserRepository,
    private val objectMapper: ObjectMapper
) {

}