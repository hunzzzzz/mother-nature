package com.nature.mother.common.exception

enum class ErrorCode(val message: String) {
    // signup
    SIGNUP_INVALID_EMAIL("유효하지 않은 이메일 형식입니다."),
    SIGNUP_INVALID_DOMAIN("카카오 혹은 네이버의 이메일을 사용하시는 경우, 소셜 로그인을 통해 회원가입을 진행해주세요."),
    SIGNUP_ALREADY_USED_EMAIL("이미 사용 중인 이메일입니다."),

    SIGNUP_EXPIRED_VERIFICATION_CODE("인증번호가 만료되었습니다."),
    SIGNUP_INVALID_VERIFICATION_CODE("인증번호가 일치하지 않습니다."),

    SIGNUP_NOT_IDENTIFIED("본인인증이 완료되지 않았습니다."),
    SIGNUP_INVALID_TWO_PASSWORDS("두 비밀번호가 일치하지 않습니다."),

    // system
    MAIL_SYSTEM_ERROR("이메일 전송 과정에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
}