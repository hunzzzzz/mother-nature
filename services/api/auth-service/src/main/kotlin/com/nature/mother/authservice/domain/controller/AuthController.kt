package com.nature.mother.authservice.domain.controller

import com.nature.mother.authservice.domain.dto.request.LoginRequest
import com.nature.mother.authservice.domain.service.KakaoLoginService
import com.nature.mother.authservice.domain.service.LoginService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val loginService: LoginService,
    private val kakaoLoginService: KakaoLoginService
) {
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ) = loginService.login(request = request).let { ResponseEntity.ok(it) }

    @GetMapping("/login/kakao")
    fun kakaoLogin(@RequestParam code: String) =
        kakaoLoginService.login(code = code).let { ResponseEntity.ok(it) }
}