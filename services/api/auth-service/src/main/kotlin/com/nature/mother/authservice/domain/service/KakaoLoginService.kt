package com.nature.mother.authservice.domain.service

import com.nature.mother.authservice.domain.dto.response.KakaoTokenResponse
import com.nature.mother.authservice.domain.dto.response.kakao.KakaoToken
import com.nature.mother.authservice.domain.dto.response.kakao.KakaoUserInfo
import com.nature.mother.authservice.global.utility.AuthProvider
import com.nature.mother.authservice.global.utility.OAuthHandler
import com.nature.mother.authservice.global.utility.OAuthHandler.KakaoURL
import com.nature.mother.common.dto.KakaoSignup
import com.nature.mother.common.model.UserType
import com.nature.mother.common.utility.KafkaProducer
import com.nature.mother.common.utility.RedisCommands
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Description
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
@RefreshScope
class KakaoLoginService(
    @Value("\${kakao.client_id}")
    private val clientId: String,
    @Value("\${kakao.redirect_uri}")
    private val redirectUri: String,

    private val authProvider: AuthProvider,
    private val kafkaProducer: KafkaProducer,
    private val oauthHandler: OAuthHandler,
    private val redisCommands: RedisCommands
) {
    private fun getToken(code: String): KakaoToken {
        val formData = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("redirect_uri", redirectUri)
            add("code", code)
        }

        return oauthHandler.getToken(
            type = UserType.KAKAO,
            url = KakaoURL.TOKEN_REQUEST_URL,
            uri = KakaoURL.TOKEN_REQUEST_URI,
            formData = formData
        )
    }

    private fun getUserInfo(accessToken: String): KakaoUserInfo {
        return oauthHandler.getUserInfo(
            type = UserType.KAKAO,
            url = KakaoURL.USER_INFO_REQUEST_URL,
            uri = KakaoURL.USER_INFO_REQUEST_URI,
            accessToken = accessToken
        )
    }

    @Description("카카오 유저 로그인")
    fun login(code: String): KakaoTokenResponse {
        val kakaoToken = getToken(code = code)
        val userInfo = getUserInfo(accessToken = kakaoToken.accessToken)
        val email = userInfo.kakaoAccount.email!!
        
        // if user is 'newcomer' -> signup
        val userKey = getKeyOfUserInfo(email = email)
        val isNewcomer = redisCommands.get(key = userKey) == null

        if (isNewcomer) {
            val data = KakaoSignup(
                email = email,
                nickname = userInfo.kakaoAccount.profile.nickname,
                imageUrl = userInfo.kakaoAccount.profile.profileImageUrl
            )
            kafkaProducer.send(topic = "kakao-signup", data = data)
        }

        // get 'user info' from redis
        val user = authProvider.getUserInfoFromRedis(email = email)

        // create 'tokens'
        val (accessToken, refreshToken) = authProvider.createToken(user = user)

        return KakaoTokenResponse.from(accessToken = accessToken, refreshToken = refreshToken, kakaoToken = kakaoToken)
    }
}