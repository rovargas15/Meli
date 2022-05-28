package com.test.meli.data.repository

import com.test.meli.app.getFailure
import com.test.meli.app.getSuccess
import com.test.meli.app.isFailure
import com.test.meli.app.isSuccess
import com.test.meli.data.endpoint.SearchProductApi
import com.test.meli.data.model.ResponseQueryDTO
import com.test.meli.domain.exception.UnknownError
import com.test.meli.domain.model.ResponseQuery
import com.test.meli.domain.repository.DomainExceptionRepository
import com.test.meli.domain.repository.SearchProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class SearchProductRepositoryImplTest {

    private val searchProductApi: SearchProductApi = mockk(relaxed = true)
    private val exception: DomainExceptionRepository = mockk(relaxed = true)
    private val searchProductRepository: SearchProductRepository =
        SearchProductRepositoryImpl(searchProductApi, exception)

    @Test
    fun giveEmptyWhenGeProductThenReturnResultListEmpty() = runBlocking {
        // Give
        val responseQueryDTO: ResponseQueryDTO = mockk()
        val query = "product"
        every { responseQueryDTO.toResponseQuery() } answers { ResponseQuery(emptyList()) }
        coEvery { searchProductApi.searchProduct(query) } answers { responseQueryDTO }

        // When
        val response = searchProductRepository.getProductQuery(query)

        // Then
        response.collect { result ->
            assert(result.isSuccess())
            Assert.assertEquals(result.getSuccess()?.results, emptyList<ResponseQuery>())
        }
        coVerify(exactly = 1) {
            searchProductApi.searchProduct(query)
            responseQueryDTO.toResponseQuery()
        }
        confirmVerified(searchProductApi, exception, responseQueryDTO)
    }

    @Test
    fun giveDataWhenGeProductThenReturnListProduct() = runBlocking {
        // Give
        val responseQueryDTO = mockk<ResponseQueryDTO> {
            every { results } returns listOf(
                mockk {
                    every { id } returns "ABC123"
                    every { title } returns "Motorola"
                    every { price } returns 12345
                    every { condition } returns "New"
                    every { acceptsMercadoPago } returns true
                    every { addressDTO } returns mockk {
                        every { cityId } returns "city"
                        every { cityName } returns "cityName"
                        every { stateId } returns "stateId"
                        every { stateName } returns "stateName"
                    }
                    every { attributeDTO } returns listOf(
                        mockk {
                            every { id } returns "1"
                            every { attributeGroupId } returns "attributeGroupId"
                            every { attributeGroupName } returns "attributeGroupName"
                            every { name } returns "name"
                            every { source } returns 1
                            every { valueId } returns "valueId"
                            every { valueName } returns "valueName"
                        }
                    )
                    every { condition } returns "New"
                    every { availableQuantity } returns 12
                    every { seller } returns mockk {
                        every { id } returns 1
                        every { eshopDTO } returns mockk {
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
                    every { soldQuantity } returns 1
                    every { stopTime } returns "stopTime"
                    every { thumbnail } returns "thumbnail"
                }
            )
        }
        val responseQuery = mockk<ResponseQuery> {
            every { results } returns listOf(
                mockk {
                    every { id } returns "ABC123"
                    every { title } returns "Motorola"
                    every { price } returns 12345
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
                            every { source } returns 1
                            every { valueId } returns "valueId"
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
                    every { soldQuantity } returns 1
                    every { stopTime } returns "stopTime"
                    every { thumbnail } returns "thumbnail"
                }
            )
        }

        val query = "product"
        every { responseQueryDTO.toResponseQuery() } answers { responseQuery }
        coEvery { searchProductApi.searchProduct(query) } answers { responseQueryDTO }

        // When
        val response = searchProductRepository.getProductQuery(query)

        // Then
        response.collect { result ->
            assert(result.isSuccess())
            Assert.assertEquals(result.getSuccess(), responseQuery)
        }
        coVerify(exactly = 1) {
            searchProductApi.searchProduct(query)
            responseQueryDTO.toResponseQuery()
        }
        confirmVerified(searchProductApi, exception, responseQueryDTO)
    }

    @Test
    fun giveDataWhenGeProductThenReturnException() = runBlocking {
        // Give
        val query = "product"
        val error = Throwable()
        coEvery { searchProductApi.searchProduct(query) } throws error
        every { exception.manageError(error) } returns UnknownError

        // When
        val response = searchProductRepository.getProductQuery(query)

        // Then
        response.collect { result ->
            assert(result.isFailure())
            Assert.assertEquals(result.getFailure(), UnknownError)
        }
        coVerify(exactly = 1) {
            searchProductApi.searchProduct(query)
            exception.manageError(error)
        }
        confirmVerified(searchProductApi, exception)
    }
}
