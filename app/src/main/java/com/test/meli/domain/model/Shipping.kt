package com.test.meli.domain.model

data class Shipping(
    val freeShipping: Boolean?,
    val logisticType: String?,
    val mode: String?,
    val storePickUp: Boolean?,
    val tags: List<String>
)
