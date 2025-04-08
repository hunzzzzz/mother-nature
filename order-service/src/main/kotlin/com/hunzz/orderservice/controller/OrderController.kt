package com.hunzz.orderservice.controller

import com.hunzz.orderservice.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products/{productId}")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping("/order")
    fun order(
        @PathVariable productId: Long,
        @RequestParam count: Int
    ): ResponseEntity<Unit> {
        val userId = 100L // TODO : 추후 수정
        val body = orderService.order(userId = userId, productId = productId, count = count)

        return ResponseEntity.ok(body)
    }
}