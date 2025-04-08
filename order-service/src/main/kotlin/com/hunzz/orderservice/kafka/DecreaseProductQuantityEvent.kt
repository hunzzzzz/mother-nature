package com.hunzz.orderservice.kafka

data class DecreaseProductQuantityEvent(
    val productId: Long,
    val count: Int
)
