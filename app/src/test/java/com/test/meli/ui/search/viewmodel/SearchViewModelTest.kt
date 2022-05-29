package com.test.meli.ui.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.meli.domain.exception.UnknownError
import com.test.meli.domain.model.Product
import com.test.meli.domain.model.ResponseQuery
import com.test.meli.domain.uc.SearchProductUC
import com.test.meli.ui.search.state.SearchState
import com.test.meli.util.MainDispatcherRule
import com.test.meli.util.getOrAwaitValue
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

    private val searchProductUC: SearchProductUC = mockk()
    private val searchViewModel = SearchViewModel(searchProductUC)

    @Test
    fun giveEmptyWhenGeProductThenReturnResultResultSuccess() = mainDispatcherRule.runBlockingTest {
        // Give
        val query = "test"
        val responseQuery: ResponseQuery = mockk()
        every { searchProductUC.invoke(query) } answers { flowOf(Result.success(responseQuery)) }

        // When
        searchViewModel.searchProduct(query)

        // Then
        val response = searchViewModel.productLiveData.getOrAwaitValue()
        assert(response is SearchState.Success)
        Assert.assertEquals((response as SearchState.Success).responseQuery, responseQuery)
        verify(exactly = 1) { searchProductUC.invoke(query) }
        confirmVerified(searchProductUC)
    }

    @Test
    fun giveDataWhenGeProductThenReturnResultFailure() {
        // Give
        val query = "test"
        every { searchProductUC.invoke(query) } answers { flowOf(Result.failure(UnknownError)) }

        // When
        searchViewModel.searchProduct(query)

        // Then
        val response = searchViewModel.productLiveData.getOrAwaitValue()
        assert(response is SearchState.Error)
        verify(exactly = 1) { searchProductUC.invoke(query) }
        confirmVerified(searchProductUC)
    }

    @Test
    fun giveSelectProductWhenGeProductThenReturnProduct() {
        // Give
        val product: Product = mockk()

        // When
        searchViewModel.selectProductDetails(product)

        // Then
        val response = searchViewModel.productDetailLiveData.getOrAwaitValue()
        Assert.assertEquals(product, response)
    }
}
