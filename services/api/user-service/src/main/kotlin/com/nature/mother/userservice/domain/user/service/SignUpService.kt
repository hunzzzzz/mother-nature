package com.nature.mother.userservice.domain.user.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.nature.mother.common.model.SimpleUserInfo
import com.nature.mother.common.exception.ErrorCode.*
import com.nature.mother.common.utility.RedisCommands
import com.nature.mother.common.variables.Logs.MAIL_ERROR_LOG
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfNicknames
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfPassword
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfUserInfo
import com.nature.mother.common.variables.RedisKeyFinder.getKeyOfVerificationCode
import com.nature.mother.userservice.domain.user.dto.request.SignUpRequest
import com.nature.mother.userservice.domain.user.model.BaseUser
import com.nature.mother.userservice.domain.user.model.User
import com.nature.mother.common.model.UserType
import com.nature.mother.userservice.domain.user.repository.UserRepository
import com.nature.mother.userservice.global.exception.InternalSystemException
import com.nature.mother.userservice.global.exception.InvalidUserInfoException
import com.nature.mother.userservice.global.utility.UserMailSender
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.MINUTES

@Service
class SignUpService(
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder,
    private val redisCommands: RedisCommands,
    private val userMailSender: UserMailSender,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private fun checkValidEmail(email: String) {
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        val isValidEmail = regex.matches(email)

        require(isValidEmail) { throw InvalidUserInfoException(SIGNUP_INVALID_EMAIL) }
    }

    private fun checkValidDomain(email: String) {
        val domain = email.substringAfter("@")
        val isValidDomain = domain.uppercase().let {
            !it.startsWith(UserType.NAVER.name) && !it.startsWith(UserType.KAKAO.name)
        }

        require(isValidDomain) { throw InvalidUserInfoException(SIGNUP_INVALID_DOMAIN) }
    }

    private fun checkEmailDuplication(email: String) {
        val passwordKey = getKeyOfPassword(email = email)
        val isNotDuplicated = redisCommands.get(key = passwordKey) == null

        require(isNotDuplicated) { throw InvalidUserInfoException(SIGNUP_ALREADY_USED_EMAIL) }
    }

    private fun checkCodeExpiration(email: String) {
        val codeKey = getKeyOfVerificationCode(email = email)
        val isNotExpired = redisCommands.get(key = codeKey) != null

        require(isNotExpired) { throw InvalidUserInfoException(SIGNUP_EXPIRED_VERIFICATION_CODE) }
    }

    private fun checkValidCode(email: String, code: String) {
        val codeKey = getKeyOfVerificationCode(email = email)
        val verificationCode = redisCommands.get(key = codeKey)
        val isValidCode = verificationCode == code

        require(isValidCode) { throw InvalidUserInfoException(SIGNUP_INVALID_VERIFICATION_CODE) }
    }

    private fun checkIdentified(isIdentified: Boolean) {
        require(isIdentified) { throw InvalidUserInfoException(SIGNUP_NOT_IDENTIFIED) }
    }

    private fun checkTwoPasswords(first: String, second: String) {
        val isSamePasswords = first == second

        require(isSamePasswords) { throw InvalidUserInfoException(SIGNUP_INVALID_TWO_PASSWORDS) }
    }

    private fun saveUserInfoInRedis(type: UserType, user: BaseUser) {
        val userKey = getKeyOfUserInfo(email = user.email)
        val nicknameKey = getKeyOfNicknames()
        val simpleUserInfo = SimpleUserInfo(
            userId = user.id!!.toString(),
            type = user.type.name,
            email = user.email,
            role = user.role.authority,
        )

        redisCommands.set(key = userKey, value = objectMapper.writeValueAsString(simpleUserInfo))
        redisCommands.sAdd(key = nicknameKey, value = user.nickname)
        if (type == UserType.NORMAL) {
            val passwordKey = getKeyOfPassword(email = user.email)
            redisCommands.set(key = passwordKey, value = (user as User).password)
        }
    }

    fun checkEmail(email: String) {
        // validate
        checkValidEmail(email = email)
        checkValidDomain(email = email)
        checkEmailDuplication(email = email)
    }

    fun sendVerificationCode(email: String) {
        // create 'mail components'
        val verificationCode = userMailSender.generateRandomNumber()
        val subject = userMailSender.getSubjectOfVerificationCode()
        val text = userMailSender.getTextOfVerificationCode(code = verificationCode)

        userMailSender.sendEmail(email = email, subject = subject, text = text)
            .onSuccess {
                // save 'verification-code' in redis (just for 3min)
                redisCommands.set(
                    key = getKeyOfVerificationCode(email = email),
                    value = verificationCode,
                    expirationTime = 3,
                    timeUnit = MINUTES
                )
            }
            .onFailure {
                logger.error("$MAIL_ERROR_LOG \n ${it.message}")
                throw InternalSystemException(MAIL_SYSTEM_ERROR)
            }
    }

    fun checkVerificationCode(email: String, code: String) {
        // validate
        checkCodeExpiration(email = email)
        checkValidCode(email = email, code = code)
    }

    fun signup(isIdentified: Boolean, request: SignUpRequest) {
        // validate
        checkIdentified(isIdentified = isIdentified)
        checkTwoPasswords(first = request.password!!, second = request.password2!!)

        // save 'user' in db
        val user = request.to(passwordEncoder = passwordEncoder).let { userRepository.save(it) }

        // save 'user' in redis
        saveUserInfoInRedis(type = UserType.NORMAL, user = user)
    }
}