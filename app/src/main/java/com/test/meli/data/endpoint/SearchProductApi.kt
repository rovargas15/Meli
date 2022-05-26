package com.test.meli.data.endpoint

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchProductApi {

    @GET(GET_SEARCH_PRODUCT)
    suspend fun searchProduct(
        @Query("q") q: String
    )
}

private const val GET_SEARCH_PRODUCT = "search"
