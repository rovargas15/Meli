package com.test.meli.domain.uc

import com.test.meli.domain.exception.UnknownError
import com.test.meli.domain.model.ResponseQuery
import com.test.meli.domain.repository.ProductRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetProductByUCTest {
    private val productRepository: ProductRepository = mockk(relaxed = true)
    private val getProductByUC = GetProductByUC(productRepository)

    @Test
    fun giveEmptyWhenGeProductThenReturnResultList() = runBlocking {
        // Give
        val query = "moto"
        val responseQuery: ResponseQuery = mockk()
        every { productRepository.getProductByQuery(query) } answers {
            flowOf(
                Result.success(
                    responseQuery
                )
            )
        }

        // When
        val response = getProductByUC.invoke(query)

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), responseQuery)
        }
        verify { productRepository.getProductByQuery(query) }
        confirmVerified(productRepository)
    }

    @Test
    fun giveDataWhenGeProductThenReturnException() = runBlocking {
        // Give
        val query = "moto"
        every { productRepository.getProductByQuery(query) } answers {
            flowOf(
                Result.failure(
                    UnknownError
                )
            )
        }

        // When
        val response = getProductByUC.invoke(query)

        // Then
        response.collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(result.exceptionOrNull(), UnknownError)
        }
        verify { productRepository.getProductByQuery(query) }
        confirmVerified(productRepository)
    }
}
