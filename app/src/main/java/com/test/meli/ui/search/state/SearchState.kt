package com.test.meli.ui.search.state

import com.test.meli.domain.model.ResponseQuery

sealed class SearchState {
    object Loading : SearchState()
    data class Error(val message: String?) : SearchState()
    data class Success(val responseQuery: ResponseQuery) : SearchState()
}
