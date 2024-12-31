package com.nature.mother.authservice.domain.controller

import com.nature.mother.authservice.domain.dto.request.LoginRequest
import com.nature.mother.authservice.domain.service.LoginService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val loginService: LoginService
) {
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ) = loginService.login(request = request).let { ResponseEntity.ok(it) }
}