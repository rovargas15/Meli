package com.test.meli.data.repository

import android.util.Log
import com.test.meli.data.endpoint.SearchProductApi
import com.test.meli.domain.repository.DomainExceptionRepository
import com.test.meli.domain.repository.SearchProductRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchProductRepositoryImpl(
    private val searchProductApi: SearchProductApi,
    private val domainExceptionRepository: DomainExceptionRepository
) : SearchProductRepository {

    override fun getProductQuery(query: String) = flow {
        val response = searchProductApi.searchProduct(query)
        emit(Result.success(response.toResponseQuery()))
    }.catch { error ->
        Log.e("SearchProductRepositoryImpl",error.message.toString())
        emit(Result.failure(domainExceptionRepository.manageError(error)))
    }
}
