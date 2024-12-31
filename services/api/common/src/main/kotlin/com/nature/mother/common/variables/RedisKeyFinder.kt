package com.nature.mother.common.variables

object RedisKeyFinder {
    // signup
    fun getKeyOfVerificationCode(email: String) = "verification_code::$email"

    // login
    fun getKeyOfPassword(email: String) = "password::$email"
    fun getKeyOfRtk(email: String) = "rtk::$email"

    // user
    fun getKeyOfUserInfo(email: String) = "user::$email" // TODO : 추후 email이 아닌 userId로 변경될 수 있음
    fun getKeyOfNicknames() = "nicknames"
}