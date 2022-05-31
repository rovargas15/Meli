package com.test.meli.data.repository

import com.test.meli.data.endpoint.ProductApi
import com.test.meli.domain.repository.DomainExceptionRepository
import com.test.meli.domain.repository.ProductRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(
    private val productApi: ProductApi,
    private val domainExceptionRepository: DomainExceptionRepository
) : ProductRepository {

    override fun getProductByQuery(query: String) = flow {
        val response = productApi.getProductByQuery(query)
        emit(Result.success(response.toResponseQuery()))
    }.catch { error ->
        emit(Result.failure(domainExceptionRepository.manageError(error)))
    }
}
