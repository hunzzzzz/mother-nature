package com.nature.mother.authservice.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.nature.mother.authservice.domain.dto.request.LoginRequest
import com.nature.mother.authservice.domain.dto.response.TokenResponse
import com.nature.mother.authservice.global.exception.LoginException
import com.nature.mother.authservice.global.utility.AuthProvider
import com.nature.mother.common.dto.SimpleUserInfo
import com.nature.mother.common.exception.ErrorCode.LOGIN_INVALID_INFO
import com.nature.mother.common.utility.RedisCommands
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfPassword
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfUserInfo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val authProvider: AuthProvider,
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder,
    private val redisCommands: RedisCommands
) {
    private fun checkEmail(email: String) {
        val userPasswordKey = getKeyOfPassword(email = email)
        val isUser = redisCommands.get(key = userPasswordKey) != null

        require(isUser) { throw LoginException(LOGIN_INVALID_INFO) }
    }

    private fun checkPassword(email: String, inputPassword: String) {
        val userPasswordKey = getKeyOfPassword(email = email)
        val userPassword = redisCommands.get(key = userPasswordKey)!!
        val isValidPassword = passwordEncoder.matches(inputPassword, userPassword)

        require(isValidPassword) { throw LoginException(LOGIN_INVALID_INFO) }
    }

    fun login(request: LoginRequest): TokenResponse {
        // validate
        checkEmail(email = request.email)
        checkPassword(email = request.email, inputPassword = request.password)

        // get 'user'
        val userKey = getKeyOfUserInfo(email = request.email)
        val user = redisCommands.get(key = userKey)
            ?.let { objectMapper.readValue(it, SimpleUserInfo::class.java) }!!

        // create 'tokens'
        val (accessToken, refreshToken) = authProvider.createToken(user = user)

        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }
}