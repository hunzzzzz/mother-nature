package com.hunzz.productservice.service

import com.hunzz.productservice.model.Product
import com.hunzz.productservice.model.property.Category
import com.hunzz.productservice.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductInitService(
    private val productRepository: ProductRepository
) {
    val products = listOf(
        Product(name = "초록개런티 단호박(850g)", category = Category.VEGETABLE, quantity = 100)
    )

    fun init() {
        productRepository.saveAll(products)
    }
}