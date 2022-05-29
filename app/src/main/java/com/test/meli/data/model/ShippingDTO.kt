package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.Shipping

@JsonClass(generateAdapter = true)
data class ShippingDTO(
    @Json(name = "free_shipping")
    val freeShipping: Boolean?,
    @Json(name = "logistic_type")
    val logisticType: String?,
    @Json(name = "mode")
    val mode: String?,
    @Json(name = "store_pick_up")
    val storePickUp: Boolean?,
    @Json(name = "tags")
    val tags: List<String>
) {

    fun toDomainShipping(): Shipping {
        return Shipping(freeShipping, logisticType, mode, storePickUp, tags)
    }
}
