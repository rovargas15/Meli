package com.test.meli.domain.model

data class Seller(
    val id: Int,
    val eshop: Eshop?,
    val permalink: String,
    val registrationDate: String?,
    val sellerReputation: SellerReputation?,
    val tags: List<String>
)
