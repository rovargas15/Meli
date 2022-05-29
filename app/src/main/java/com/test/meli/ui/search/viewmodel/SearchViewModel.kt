package com.test.meli.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.meli.domain.model.Product
import com.test.meli.domain.uc.SearchProductUC
import com.test.meli.ui.search.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductUC: SearchProductUC
) : ViewModel() {

    private val _productLiveData by lazy { MutableLiveData<SearchState>() }
    val productLiveData: LiveData<SearchState>
        get() = _productLiveData

    private val _productDetailLiveData by lazy { MutableLiveData<Product>() }
    val productDetailLiveData: LiveData<Product>
        get() = _productDetailLiveData

    fun searchProduct(query: String) {
        searchProductUC.invoke(query).map { result ->
            result.fold(
                onSuccess = {
                    _productLiveData.value = SearchState.Success(it)
                },
                onFailure = {
                    _productLiveData.value = SearchState.Error
                }
            )
        }.onStart {
            _productLiveData.value = SearchState.Loading
        }.launchIn(viewModelScope)
    }

    fun selectProductDetails(product: Product) {
        _productDetailLiveData.value = product
    }
}
