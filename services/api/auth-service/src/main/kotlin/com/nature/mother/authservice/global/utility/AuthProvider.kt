package com.nature.mother.authservice.global.utility

import com.nature.mother.common.dto.SimpleUserInfo
import com.nature.mother.common.utility.JwtProvider
import com.nature.mother.common.utility.RedisCommands
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfRtk
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class AuthProvider(
    @Value("\${jwt.expiration-time.rtk}")
    private val expirationTime: Long,

    private val jwtProvider: JwtProvider,
    private val redisCommands: RedisCommands
) {
    fun createToken(user: SimpleUserInfo): Pair<String, String> {
        val rtkKey = getKeyOfRtk(email = user.email)
        val accessToken = jwtProvider.createAccessToken(tokenUserInfo = user)
        val refreshToken = jwtProvider.createRefreshToken(tokenUserInfo = user)

        // save 'rtk' in redis
        redisCommands.set(
            key = rtkKey,
            value = refreshToken,
            expirationTime = expirationTime,
            timeUnit = TimeUnit.MILLISECONDS
        )

        return Pair(accessToken, refreshToken)
    }
}