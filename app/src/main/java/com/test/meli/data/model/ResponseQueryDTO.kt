package com.test.meli.data.model

import com.squareup.moshi.Json
import com.test.meli.domain.model.ResponseQuery

data class ResponseQueryDTO(
    @Json(name = "results")
    val results: List<ResultDTO>,
) {
    fun toResponseQuery(): ResponseQuery {
        return ResponseQuery(
            results.map {
                it.toResult()
            }
        )
    }
}
