package com.test.meli.ui.main.state

import com.test.meli.domain.model.Product

sealed interface SearchState {
    object Init : SearchState
    object Loader : SearchState
    object Error : SearchState
    data class Success(val products: List<Product>) : SearchState
}
