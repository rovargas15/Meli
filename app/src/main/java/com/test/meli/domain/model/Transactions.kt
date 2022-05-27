package com.test.meli.domain.model

data class Transactions(
    val canceled: Int,
    val completed: Int,
    val period: String,
    val total: Int
)
