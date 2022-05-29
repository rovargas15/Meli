package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.Product

@JsonClass(generateAdapter = true)
data class ResultDTO(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "price")
    val price: Double,
    @Json(name = "seller")
    val seller: SellerDTO,
    @Json(name = "condition")
    val condition: String,
    @Json(name = "accepts_mercadopago")
    val acceptsMercadoPago: Boolean?,
    @Json(name = "address")
    val address: AddressDTO?,
    @Json(name = "attributes")
    val attribute: List<AttributeDTO>?,
    @Json(name = "available_quantity")
    val availableQuantity: Int?,
    @Json(name = "shipping")
    val shipping: ShippingDTO?,
    @Json(name = "thumbnail")
    val thumbnail: String,
) {

    fun toResult(): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            condition = condition,
            acceptsMercadoPago = acceptsMercadoPago ?: false,
            address = address?.toDomainAddress(),
            attributes = attribute?.map { it.toDomainAttribute() } ?: emptyList(),
            availableQuantity = availableQuantity ?: 0,
            seller = seller.toDomainSeller(),
            shipping = shipping?.toDomainShipping(),
            thumbnail = thumbnail
        )
    }
}
