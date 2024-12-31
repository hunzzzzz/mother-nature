package com.nature.mother.authservice.global.utility

import com.nature.mother.authservice.global.exception.LoginException
import com.nature.mother.common.exception.ErrorCode.OAUTH_LOGIN_ERROR
import com.nature.mother.common.model.UserType
import com.nature.mother.common.variables.Logs.KAKAO_LOGIN_ERROR_LOG
import com.nature.mother.common.variables.Logs.NAVER_LOGIN_ERROR_LOG
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.apache.http.HttpHeaders.CONTENT_TYPE
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters.fromFormData
import org.springframework.web.reactive.function.client.WebClient.create
import reactor.core.publisher.Mono

@Component
class OAuthHandler {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    object KakaoURL {
        const val TOKEN_REQUEST_URL = "https://kauth.kakao.com"
        const val TOKEN_REQUEST_URI = "/oauth/token"

        const val USER_INFO_REQUEST_URL = "https://kapi.kakao.com"
        const val USER_INFO_REQUEST_URI = "/v2/user/me?secure_resource=true"
    }

    fun getErrorLog(type: UserType) = when (type) {
        UserType.KAKAO -> KAKAO_LOGIN_ERROR_LOG
        UserType.NAVER -> NAVER_LOGIN_ERROR_LOG
        else -> ""
    }

    final inline fun <reified T> getToken(
        type: UserType,
        url: String,
        uri: String,
        formData: LinkedMultiValueMap<String, String>?
    ): T {
        return create(url)
            .post()
            .uri(uri)
            .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
            .body(fromFormData(formData ?: LinkedMultiValueMap()))
            .retrieve()
            .bodyToMono(T::class.java)
            .onErrorResume { throwable ->
                logger.error("${getErrorLog(type = type)} \n ${throwable.message}")
                Mono.error(LoginException(OAUTH_LOGIN_ERROR))
            }
            .block()!!
    }

    final inline fun <reified T> getUserInfo(
        type: UserType,
        url: String,
        uri: String,
        accessToken: String
    ): T {
        return create(url)
            .post()
            .uri(uri)
            .header(AUTHORIZATION, "Bearer $accessToken")
            .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
            .retrieve()
            .bodyToMono(T::class.java)
            .onErrorResume { throwable ->
                logger.error("${getErrorLog(type = type)} \n ${throwable.message}")
                Mono.error(LoginException(OAUTH_LOGIN_ERROR))
            }
            .block()!!
    }
}