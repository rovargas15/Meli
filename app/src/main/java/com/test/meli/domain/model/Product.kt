package com.test.meli.domain.model

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val condition: String,
    val acceptsMercadoPago: Boolean,
    val address: Address?,
    val attributes: List<Attribute>,
    val availableQuantity: Int,
    val seller: Seller,
    val shipping: Shipping?,
    val thumbnail: String
)
