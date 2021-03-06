package com.test.meli.data.repository

import com.test.meli.data.endpoint.ProductApi
import com.test.meli.data.model.ResponseQueryDTO
import com.test.meli.domain.exception.UnknownError
import com.test.meli.domain.model.ResponseQuery
import com.test.meli.domain.repository.DomainExceptionRepository
import com.test.meli.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ProductRepositoryImplTest {

    private val productApi: ProductApi = mockk(relaxed = true)
    private val exception: DomainExceptionRepository = mockk(relaxed = true)
    private val productRepository: ProductRepository =
        ProductRepositoryImpl(productApi, exception)

    @Test
    fun giveEmptyWhenGeProductThenReturnResultListEmpty() = runBlocking {
        // Give
        val responseQueryDTO: ResponseQueryDTO = mockk()
        val query = "product"
        every { responseQueryDTO.toResponseQuery() } answers { ResponseQuery(emptyList()) }
        coEvery { productApi.getProductByQuery(query) } answers { responseQueryDTO }

        // When
        val response = productRepository.getProductByQuery(query)

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull()?.products, emptyList<ResponseQuery>())
        }
        coVerify(exactly = 1) {
            productApi.getProductByQuery(query)
            responseQueryDTO.toResponseQuery()
        }
        confirmVerified(productApi, exception, responseQueryDTO)
    }

    @Test
    fun giveDataWhenGeProductThenReturnListProduct() = runBlocking {
        // Give
        val responseQueryDTO = mockk<ResponseQueryDTO> {
            every { results } returns listOf(
                mockk {
                    every { id } returns "ABC123"
                    every { title } returns "Motorola"
                    every { price } returns 12345.0
                    every { condition } returns "New"
                    every { acceptsMercadoPago } returns true
                    every { address } returns mockk {
                        every { cityName } returns "cityName"
                        every { stateId } returns "stateId"
                        every { stateName } returns "stateName"
                    }
                    every { attribute } returns listOf(
                        mockk {
                            every { id } returns "1"
                            every { attributeGroupId } returns "attributeGroupId"
                            every { attributeGroupName } returns "attributeGroupName"
                            every { name } returns "name"
                            every { valueName } returns "valueName"
                        }
                    )
                    every { condition } returns "New"
                    every { availableQuantity } returns 12
                    every { seller } returns mockk {
                        every { id } returns 1
                        every { eshop } returns mockk {
                            every { eshopId } returns 1
                            every { eshopLogoUrl } returns "url"
                            every { nickName } returns "nickName"
                        }
                        every { permalink } returns "permalink"
                        every { registrationDate } returns "registrationDate"
                        every { sellerReputation } returns mockk {
                            every { levelId } returns "1"
                            every { powerSellerStatus } returns "status"
                            every { tags } returns emptyList()
                        }
                    }
                    every { shipping } returns mockk {
                        every { freeShipping } returns true
                        every { logisticType } returns "logisticType"
                        every { mode } returns "mode"
                        every { storePickUp } returns true
                        every { tags } returns emptyList()
                    }
                    every { thumbnail } returns "thumbnail"
                }
            )
        }
        val responseQuery = mockk<ResponseQuery> {
            every { products } returns listOf(
                mockk {
                    every { id } returns "ABC123"
                    every { title } returns "Motorola"
                    every { price } returns 12345.0
                    every { condition } returns "New"
                    every { acceptsMercadoPago } returns true
                    every { address } returns mockk {
                        every { cityName } returns "cityName"
                        every { stateId } returns "stateId"
                        every { stateName } returns "stateName"
                    }
                    every { attributes } returns listOf(
                        mockk {
                            every { id } returns "1"
                            every { attributeGroupId } returns "attributeGroupId"
                            every { attributeGroupName } returns "attributeGroupName"
                            every { name } returns "name"
                            every { valueName } returns "valueName"
                        }
                    )
                    every { condition } returns "New"
                    every { availableQuantity } returns 12
                    every { seller } returns mockk {
                        every { id } returns 1
                        every { eshop } returns mockk {
                            every { eshopId } returns 1
                            every { eshopLogoUrl } returns "url"
                            every { nickName } returns "nickName"
                        }
                        every { permalink } returns "permalink"
                        every { registrationDate } returns "registrationDate"
                        every { sellerReputation } returns mockk {
                            every { levelId } returns "1"
                            every { powerSellerStatus } returns "status"
                            every { tags } returns emptyList()
                        }
                    }
                    every { shipping } returns mockk {
                        every { freeShipping } returns true
                        every { logisticType } returns "logisticType"
                        every { mode } returns "mode"
                        every { storePickUp } returns true
                        every { tags } returns emptyList()
                    }
                    every { thumbnail } returns "thumbnail"
                }
            )
        }

        val query = "product"
        every { responseQueryDTO.toResponseQuery() } answers { responseQuery }
        coEvery { productApi.getProductByQuery(query) } answers { responseQueryDTO }

        // When
        val response = productRepository.getProductByQuery(query)

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), responseQuery)
        }
        coVerify(exactly = 1) {
            productApi.getProductByQuery(query)
            responseQueryDTO.toResponseQuery()
        }
        confirmVerified(productApi, exception, responseQueryDTO)
    }

    @Test
    fun giveDataWhenGeProductThenReturnException() = runBlocking {
        // Give
        val query = "product"
        val error = Throwable()
        coEvery { productApi.getProductByQuery(query) } throws error
        every { exception.manageError(error) } returns UnknownError

        // When
        val response = productRepository.getProductByQuery(query)

        // Then
        response.collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(result.exceptionOrNull(), UnknownError)
            result.onFailure { error ->
                Assert.assertEquals(error, UnknownError)
            }
        }
        coVerify(exactly = 1) {
            productApi.getProductByQuery(query)
            exception.manageError(error)
        }
        confirmVerified(productApi, exception)
    }
}
