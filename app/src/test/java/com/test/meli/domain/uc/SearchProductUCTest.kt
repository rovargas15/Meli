package com.test.meli.domain.uc

import com.test.meli.domain.exception.UnknownError
import com.test.meli.domain.model.ResponseQuery
import com.test.meli.domain.repository.SearchProductRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class SearchProductUCTest {
    private val searchProductRepository: SearchProductRepository = mockk(relaxed = true)
    private val searchProductUC = SearchProductUC(searchProductRepository)

    @Test
    fun giveEmptyWhenGeProductThenReturnResultList() = runBlocking {
        // Give
        val query = "moto"
        val responseQuery: ResponseQuery = mockk()
        every { searchProductRepository.getProductQuery(query) } answers {
            flowOf(
                Result.success(
                    responseQuery
                )
            )
        }

        // When
        val response = searchProductUC.invoke(query)

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), responseQuery)
        }
        verify { searchProductRepository.getProductQuery(query) }
        confirmVerified(searchProductRepository)
    }

    @Test
    fun giveDataWhenGeProductThenReturnException() = runBlocking {
        // Give
        val query = "moto"
        every { searchProductRepository.getProductQuery(query) } answers {
            flowOf(
                Result.failure(
                    UnknownError
                )
            )
        }

        // When
        val response = searchProductUC.invoke(query)

        // Then
        response.collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(result.exceptionOrNull(), UnknownError)
        }
        verify { searchProductRepository.getProductQuery(query) }
        confirmVerified(searchProductRepository)
    }
}
