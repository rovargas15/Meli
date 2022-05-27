package com.test.meli.data.model

import com.squareup.moshi.Json
import com.test.meli.domain.model.Result

data class ResultDTO(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "condition")
    val condition: String,
    @Json(name = "accepts_mercadopago")
    val acceptsMercadoPago: Boolean,
    @Json(name = "address")
    val addressDTO: AddressDTO,
    @Json(name = "attributes")
    val attributeDTO: List<AttributeDTO>,
    @Json(name = "available_quantity")
    val availableQuantity: Int,
    @Json(name = "seller")
    val seller: SellerDTO,
    @Json(name = "shipping")
    val shipping: ShippingDTO,
    @Json(name = "sold_quantity")
    val soldQuantity: Int,
    @Json(name = "stop_time")
    val stopTime: String,
    @Json(name = "thumbnail")
    val thumbnail: String,
) {

    fun toResult(): Result {
        return Result(
            id,
            title,
            price,
            condition,
            acceptsMercadoPago,
            addressDTO.toDomainAddress(),
            attributeDTO.map { it.toDomainAttribute() },
            availableQuantity,
            seller.toDomainSeller(),
            shipping.toDomainShipping(),
            soldQuantity,
            stopTime,
            thumbnail
        )
    }
}
