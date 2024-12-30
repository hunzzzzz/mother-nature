package com.nature.mother.userservice.domain.user.controller

import com.nature.mother.userservice.domain.user.dto.request.SignUpRequest
import com.nature.mother.userservice.domain.user.service.SignUpService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/signup")
class SignUpController(
    private val signUpService: SignUpService
) {
    @GetMapping("/check/email")
    fun checkEmail(@RequestParam email: String) =
        signUpService.checkEmail(email = email).let { ResponseEntity.ok(it) }

    @GetMapping("/code")
    fun sendVerificationCode(
        @RequestParam email: String
    ) =
        signUpService.sendVerificationCode(email = email).let { ResponseEntity.ok(it) }

    @GetMapping("/check/code")
    fun checkVerificationCode(
        @RequestParam email: String,
        @RequestParam code: String
    ) = signUpService.checkVerificationCode(email = email, code = code).let { ResponseEntity.ok(it) }

    @PostMapping
    fun signup(
        @RequestParam isIdentified: Boolean,
        @Valid @RequestBody request: SignUpRequest
    ) = signUpService.signup(isIdentified = isIdentified, request = request)
        .let { ResponseEntity.created(URI.create("/login")).body(it) }
}