package com.test.meli.ui.main.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.meli.domain.model.Product
import com.test.meli.domain.uc.GetProductByUC
import com.test.meli.ui.main.state.SearchEvent
import com.test.meli.ui.main.state.SearchEvent.ProductByQuery
import com.test.meli.ui.main.state.SearchEvent.Reload
import com.test.meli.ui.main.state.SearchEvent.SelectProduct
import com.test.meli.ui.main.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getProductByUC: GetProductByUC,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    var uiStateProductDetail: Product? by mutableStateOf(null)
        private set

    private val _viewState: MutableState<SearchState> = mutableStateOf(SearchState.Init)
    val viewState: State<SearchState> = _viewState

    fun process(event: SearchEvent) {
        when (event) {
            is ProductByQuery -> getProductQuery(query = event.query)
            is Reload -> getProductQuery("")
            is SelectProduct -> selectProductDetails(product = event.product)
        }
    }

    private fun getProductQuery(query: String) {
        getProductByUC.invoke(query).map { result ->
            result.fold(
                onSuccess = {
                    // uiStateProduct = it.products
                    _viewState.value = SearchState.Success(it.products)
                },
                onFailure = {
                    Timber.tag("SearchViewModel").e(it)
                    _viewState.value = SearchState.Error
                    // uiStateError = it.message ?: ""
                }
            )
        }.onStart {
            _viewState.value = SearchState.Loader
            // uiStateLoader = true
        }.flowOn(coroutineDispatcher).launchIn(viewModelScope)
    }

    private fun selectProductDetails(product: Product) {
        uiStateProductDetail = product
    }
}
