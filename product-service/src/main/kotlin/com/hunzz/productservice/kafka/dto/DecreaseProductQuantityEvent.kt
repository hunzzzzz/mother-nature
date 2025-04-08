package com.hunzz.productservice.kafka.dto

data class DecreaseProductQuantityEvent(
    val productId: Long,
    val count: Int
)
