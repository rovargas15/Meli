package com.test.meli.ui.search.state

import com.test.meli.domain.model.Product
import com.test.meli.domain.model.ResponseQuery

sealed class SearchState {
    object Loading : SearchState()
    object Error : SearchState()
    data class Success(val responseQuery: ResponseQuery) : SearchState()
}

data class SearchStateCompose(
    var products: List<Product> = emptyList(),
    var isLoading: Boolean = false,
    var error: String = ""
)
