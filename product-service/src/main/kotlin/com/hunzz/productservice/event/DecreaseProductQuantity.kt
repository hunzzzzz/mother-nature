package com.hunzz.productservice.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.hunzz.productservice.kafka.dto.DecreaseProductQuantityEvent
import com.hunzz.productservice.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DecreaseProductQuantity(
    private val objectMapper: ObjectMapper,
    private val productRepository: ProductRepository
) {
    @KafkaListener(topics = ["decrease-product-quantity"], groupId = "decrease-product-quantity")
    @Transactional
    fun decreaseProductQuantity(message: String) {
        val data = objectMapper.readValue(message, DecreaseProductQuantityEvent::class.java)
        val product = productRepository.findByIdOrNull(id = data.productId)
            ?: throw IllegalArgumentException("Product with id ${data.productId} not found") // TODO

        product.updateQuantity(count = data.count * (-1))
    }
}