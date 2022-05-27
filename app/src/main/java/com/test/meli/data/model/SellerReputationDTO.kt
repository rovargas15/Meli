package com.test.meli.data.model

import com.squareup.moshi.Json
import com.test.meli.domain.model.SellerReputation

data class SellerReputationDTO(
    @Json(name = "level_id")
    val levelId: String,
    @Json(name = "power_seller_status")
    val powerSellerStatus: String?,
    @Json(name = "transactions")
    val transactions: TransactionDTO
) {
    fun toDomainSellerReputation(): SellerReputation {
        return SellerReputation(levelId, powerSellerStatus, transactions.toDomainTransaction())
    }
}
