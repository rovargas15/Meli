package com.test.meli.data.model

import com.squareup.moshi.Json
import com.test.meli.domain.model.Eshop

data class EshopDTO(
    @Json(name = "eshop_id")
    val eshopId: Int,
    @Json(name = "eshop_logo_url")
    val eshopLogoUrl: String,
    @Json(name = "nick_name")
    val nickName: String
) {
    fun toDomainEshop(): Eshop {
        return Eshop(eshopId, eshopLogoUrl, nickName)
    }
}
