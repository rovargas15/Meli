package com.test.meli.data.model

import com.squareup.moshi.Json
import com.test.meli.domain.model.ResponseQuery

data class ResponseQueryDTO(
    @Json(name = "query")
    val query: String,
    @Json(name = "results")
    val results: List<ResultDTO>,
) {
    fun toResponseQuery(): ResponseQuery {
        return ResponseQuery(
            query,
            results.map {
                it.toResult()
            }
        )
    }
}
