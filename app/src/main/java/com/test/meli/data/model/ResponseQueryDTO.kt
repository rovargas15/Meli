package com.test.meli.data.model

import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.ResponseQuery

@JsonClass(generateAdapter = true)
data class ResponseQueryDTO(
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
