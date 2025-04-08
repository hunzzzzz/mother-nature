package com.hunzz.orderservice.service

import com.hunzz.orderservice.kafka.DecreaseProductQuantityEvent
import com.hunzz.orderservice.kafka.KafkaProducer
import com.hunzz.orderservice.model.Order
import com.hunzz.orderservice.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val kafkaProducer: KafkaProducer,
    private val orderRepository: OrderRepository
) {
    private fun saveOrderEntity(userId: Long, productId: Long, count: Int) {
        val order = Order(
            userId = userId,
            productId = productId,
            count = count
        )

        orderRepository.save(order)
    }

    private fun decreaseProductQuantity(productId: Long, count: Int) {
        val data = DecreaseProductQuantityEvent(productId = productId, count = count)

        kafkaProducer.send("decrease-product-quantity", data)
    }

    @Transactional
    fun order(userId: Long, productId: Long, count: Int) {
        saveOrderEntity(userId = userId, productId = productId, count = count)
        decreaseProductQuantity(productId = productId, count = count)
    }
}