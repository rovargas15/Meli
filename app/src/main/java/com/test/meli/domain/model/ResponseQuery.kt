package com.test.meli.domain.model

data class ResponseQuery(
    val query: String,
    val results: List<Result>,
)
