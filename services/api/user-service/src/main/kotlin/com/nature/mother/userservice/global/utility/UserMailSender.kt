package com.nature.mother.userservice.global.utility

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class UserMailSender(
    private val mailSender: JavaMailSender
) {
    fun generateRandomNumber() = (100000..999999).random().toString()
    fun getSubjectOfVerificationCode() = "[MotherNature] 본인 인증을 위한 메일입니다."
    fun getTextOfVerificationCode(code: String) = "다음 인증 코드 [${code}]를 웹 화면에 입력하면 인증이 완료됩니다."

    @Async
    fun sendEmail(email: String, subject: String, text: String): Result<Unit> {
        return kotlin.runCatching {
            mailSender.createMimeMessage().apply {
                MimeMessageHelper(this, false, "UTF-8").apply {
                    setTo(email)
                    setSubject(subject)
                    setText(text)
                }
            }.let(mailSender::send)
        }
    }
}