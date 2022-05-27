package com.test.meli.data.repository

import com.test.meli.app.Result
import com.test.meli.data.endpoint.SearchProductApi
import com.test.meli.domain.model.ResponseQuery
import com.test.meli.domain.repository.DomainExceptionRepository
import com.test.meli.domain.repository.SearchProductRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchProductRepositoryImpl(
    private val searchProductApi: SearchProductApi,
    private val domainExceptionRepository: DomainExceptionRepository
) : SearchProductRepository {

    override fun getProductQuery(query: String) = flow<Result<ResponseQuery>> {
        val response = searchProductApi.searchProduct(query)
        emit(Result.Success(response.toResponseQuery()))
    }.catch { error ->
        emit(Result.Failure(domainExceptionRepository.manageError(error)))
    }
}
