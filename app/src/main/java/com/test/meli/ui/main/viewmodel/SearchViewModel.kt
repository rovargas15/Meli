package com.test.meli.ui.main.viewmodel

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

    var uiStateProduct by mutableStateOf(emptyList<Product>())
        private set

    var uiStateLoader by mutableStateOf(false)
        private set

    var uiStateError by mutableStateOf("")
        private set

    var uiStateProductDetail: Product? by mutableStateOf(null)
        private set

    fun process(event: SearchEvent) {
        when (event) {
            is ProductByQuery -> getProductQuery(query = event.query)
            is Reload -> getProductQuery("")
        }
    }

    private fun getProductQuery(query: String) {
        getProductByUC.invoke(query).map { result ->
            result.fold(
                onSuccess = {
                    uiStateProduct = it.products
                },
                onFailure = {
                    Timber.tag("SearchViewModel").e(it)
                    uiStateError = it.message ?: ""
                }
            )
        }.onStart {
            uiStateLoader = true
        }.flowOn(coroutineDispatcher).launchIn(viewModelScope)
    }

    fun selectProductDetails(product: Product) {
        uiStateProductDetail = product
    }
}
