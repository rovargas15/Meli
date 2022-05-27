package com.test.meli.data.model

import com.squareup.moshi.Json
import com.test.meli.domain.model.Transactions

data class TransactionDTO(
    @Json(name = "canceled")
    val canceled: Int,
    @Json(name = "completed")
    val completed: Int,
    @Json(name = "period")
    val period: String,
    @Json(name = "total")
    val total: Int
) {

    fun toDomainTransaction(): Transactions {
        return Transactions(canceled, completed, period, total)
    }
}
