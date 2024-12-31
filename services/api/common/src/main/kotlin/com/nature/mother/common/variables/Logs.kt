package com.nature.mother.common.variables

object Logs {
    const val MAIL_ERROR_LOG = "[Error] 메일 시스템이 정상적으로 작동하지 않습니다."
    const val KAKAO_LOGIN_ERROR_LOG = "[Error] 카카오 로그인이 정상적으로 작동하지 않습니다."
    const val NAVER_LOGIN_ERROR_LOG = "[Error] 네이버 로그인이 정상적으로 작동하지 않습니다."

    fun getKafkaLog(topic: String) = "[Kafka] '${topic}' 메세지가 전송되었습니다."
}