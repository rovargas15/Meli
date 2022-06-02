package com.test.meli.ui.main.state

sealed interface SearchEvent {
    class ProductByQuery(val query: String) : SearchEvent
    class Reload(val query: String) : SearchEvent
}
