package com.nature.mother.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class PasswordEncryptionConfig {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}