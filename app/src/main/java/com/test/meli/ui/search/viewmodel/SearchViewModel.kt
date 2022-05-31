package com.test.meli.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.meli.domain.model.Product
import com.test.meli.domain.uc.GetProductByUC
import com.test.meli.ui.search.state.SearchState
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

    private val _productLiveData by lazy { MutableLiveData<SearchState>() }
    val productLiveData: LiveData<SearchState>
        get() = _productLiveData

    private val _productDetailLiveData by lazy { MutableLiveData<Product>() }
    val productDetailLiveData: LiveData<Product>
        get() = _productDetailLiveData

    fun getProductQuery(query: String) {
        getProductByUC.invoke(query).map { result ->
            result.fold(
                onSuccess = {
                    _productLiveData.postValue(SearchState.Success(it))
                },
                onFailure = {
                    Timber.tag("SearchViewModel").e(it)
                    _productLiveData.postValue(SearchState.Error)
                }
            )
        }.onStart {
            _productLiveData.postValue(SearchState.Loading)
        }.flowOn(coroutineDispatcher).launchIn(viewModelScope)
    }

    fun selectProductDetails(product: Product) {
        _productDetailLiveData.value = product
    }
}
