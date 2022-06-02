package com.test.meli.ui.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.meli.domain.exception.DomainException
import com.test.meli.domain.model.Product
import com.test.meli.domain.model.ResponseQuery
import com.test.meli.domain.uc.GetProductByUC
import com.test.meli.ui.main.viewmodel.SearchViewModel
import com.test.meli.util.MainDispatcherRule
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProductByUC: GetProductByUC = mockk()
    private val searchViewModel =
        SearchViewModel(getProductByUC, mainDispatcherRule.testDispatcher)

    @Test
    fun giveEmptyWhenGeProductThenReturnResultResultSuccess() = mainDispatcherRule.runBlockingTest {
        // Give
        val query = "test"
        val responseQuery: ResponseQuery = mockk()
        every { responseQuery.products } returns listOf()
        every { getProductByUC.invoke(query) } answers { flowOf(Result.success(responseQuery)) }

        // When
        searchViewModel.getProductQuery(query)

        // Then
        val response = searchViewModel.uiStateProduct
        Assert.assertEquals(response, emptyList<Product>())
        verify(exactly = 1) { getProductByUC.invoke(query) }
        confirmVerified(getProductByUC)
    }

    @Test
    fun giveDataWhenGeProductThenReturnResultFailure() {
        // Give
        val query = "test"
        every { getProductByUC.invoke(query) } answers { flowOf(Result.failure(DomainException("error"))) }

        // When
        searchViewModel.getProductQuery(query)

        // Then
        val response = searchViewModel.uiStateError
        Assert.assertEquals(response, "error")
        verify(exactly = 1) { getProductByUC.invoke(query) }
        confirmVerified(getProductByUC)
    }

    @Test
    fun giveSelectProductWhenGeProductThenReturnProduct() {
        // Give
        val product: Product = mockk()

        // When
        searchViewModel.selectProductDetails(product)

        // Then
        val response = searchViewModel.uiStateProductDetail
        Assert.assertEquals(product, response)
    }
}
