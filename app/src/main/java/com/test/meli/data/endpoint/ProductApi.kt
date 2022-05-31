package com.test.meli.data.endpoint

import com.test.meli.data.model.ResponseQueryDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET(GET_SEARCH_PRODUCT)
    suspend fun getProductByQuery(
        @Query("q") q: String
    ): ResponseQueryDTO
}

private const val GET_SEARCH_PRODUCT = "sites/MLA/search"
