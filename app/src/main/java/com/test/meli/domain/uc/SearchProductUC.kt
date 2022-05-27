package com.test.meli.domain.uc

import com.test.meli.domain.repository.SearchProductRepository

class SearchProductUC(
    private val searchProductRepository: SearchProductRepository
) {

    fun invoke(query: String) = searchProductRepository.getProductQuery(query)
}
