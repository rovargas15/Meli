package com.test.meli.ui.main.state

import com.test.meli.domain.model.Product

sealed class SearchEvent {
    class ProductByQuery(val query: String) : SearchEvent()
    class Reload(val query: String) : SearchEvent()
    class SelectProduct(val product: Product) : SearchEvent()
}
