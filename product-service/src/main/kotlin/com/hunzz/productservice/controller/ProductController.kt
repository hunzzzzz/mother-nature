package com.hunzz.productservice.controller

import com.hunzz.productservice.service.ProductInitService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productInitService: ProductInitService
) {
    @GetMapping("/init")
    fun init(): ResponseEntity<Unit> {
        val body = productInitService.init()

        return ResponseEntity.ok(body)
    }
}