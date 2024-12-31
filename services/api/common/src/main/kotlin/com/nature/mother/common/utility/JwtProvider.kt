package com.nature.mother.common.utility

import com.nature.mother.common.dto.SimpleUserInfo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
@RefreshScope
class JwtProvider(
    @Value("\${jwt.secret.key}")
    private val secretKey: String,

    @Value("\${jwt.issuer}")
    private val issuer: String,

    @Value("\${jwt.expiration-time.atk}")
    private val expirationTimeOfAtk: Int,

    @Value("\${jwt.expiration-time.rtk}")
    private val expirationTimeOfRtk: Int
) {
    private val key: SecretKey =
        Base64.getDecoder().decode(secretKey).let { Keys.hmacShaKeyFor(it) }

    private fun createToken(userInfo: SimpleUserInfo, expirationTime: Int) =
        Jwts.builder().let {
            it.subject(userInfo.userId)
            it.claims(
                Jwts.claims().add(
                    mapOf(
                        "type" to userInfo.type,
                        "role" to userInfo.role,
                        "email" to userInfo.email,
                    )
                ).build()
            )
            it.expiration(Date(Date().time + expirationTime))
            it.issuedAt(Date())
            it.issuer(issuer)
            it.signWith(key)
            it.compact()
        }.let { jwt -> "Bearer $jwt" }

    fun createAccessToken(tokenUserInfo: SimpleUserInfo) =
        createToken(userInfo = tokenUserInfo, expirationTime = expirationTimeOfAtk)

    fun createRefreshToken(tokenUserInfo: SimpleUserInfo) =
        createToken(userInfo = tokenUserInfo, expirationTimeOfRtk)
}