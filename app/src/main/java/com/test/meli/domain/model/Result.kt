package com.test.meli.domain.model

data class Result(
    val id: String,
    val title: String,
    val price: Int,
    val condition: String,
    val acceptsMercadoPago: Boolean,
    val address: Address,
    val attributes: List<Attribute>,
    val availableQuantity: Int,
    val seller: Seller,
    val shipping: Shipping,
    val soldQuantity: Int,
    val stopTime: String,
    val thumbnail: String
)
