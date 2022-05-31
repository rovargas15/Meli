package com.test.meli.domain.uc

import com.test.meli.domain.repository.ProductRepository

class GetProductByUC(
    private val productRepository: ProductRepository
) {

    fun invoke(query: String) = productRepository.getProductByQuery(query)
}
