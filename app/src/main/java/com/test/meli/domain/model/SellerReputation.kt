package com.test.meli.domain.model

data class SellerReputation(
    val levelId: String,
    val powerSellerStatus: String?,
    val transactions: Transactions
)
