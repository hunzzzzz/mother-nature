package com.nature.mother.authservice.global.utility

import com.fasterxml.jackson.databind.ObjectMapper
import com.nature.mother.authservice.global.exception.LoginException
import com.nature.mother.common.exception.ErrorCode.OAUTH_LOGIN_ERROR
import com.nature.mother.common.model.SimpleUserInfo
import com.nature.mother.common.utility.JwtProvider
import com.nature.mother.common.utility.RedisCommands
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfRtk
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class AuthProvider(
    @Value("\${jwt.expiration-time.rtk}")
    private val expirationTime: Long,

    private val jwtProvider: JwtProvider,
    private val objectMapper: ObjectMapper,
    private val redisCommands: RedisCommands
) {
    /**
     * 드물게, user-service에서 해당 카카오 유저의 정보를 redis에 저장하는 로직이 지연되어,
     * 아래 코드에서 NPE가 발생하는 경우가 있기 때문에, 최대 3번의 재시도 과정을 거친다.
     */
    fun getUserInfoFromRedis(email: String): SimpleUserInfo {
        var user: SimpleUserInfo?
        val userKey = getKeyOfUserInfo(email = email)

        repeat(3) {
            user = redisCommands.get(key = userKey)
                ?.let { objectMapper.readValue(it, SimpleUserInfo::class.java) }

            if (user != null) return user!!
            else Thread.sleep(500) // 0.5s
        }

        throw LoginException(OAUTH_LOGIN_ERROR)
    }

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