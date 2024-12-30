package com.nature.mother.userservice.global.exception

import com.nature.mother.common.exception.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    // Validation 실패
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException) =
        ErrorResponse(message = e.fieldErrors.first().defaultMessage!!, statusCode = "400 Bad Request")

    // 유저 관련 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserInfoException::class)
    fun handleInvalidUserInfoException(e: InvalidUserInfoException) =
        ErrorResponse(message = e.message!!, statusCode = "400 Bad Request")

    // 시스템 내부 에러 (메일)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalSystemException::class)
    fun handleInternalSystemException(e: InternalSystemException) =
        ErrorResponse(message = e.message!!, statusCode = "500 Internal Server Error")
}