package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.Eshop

@JsonClass(generateAdapter = true)
data class EshopDTO(
    @Json(name = "eshop_id")
    val eshopId: Int?,
    @Json(name = "nick_name")
    val nickName: String?,
    @Json(name = "eshop_logo_url")
    val eshopLogoUrl: String?
) {
    fun toDomainEshop(): Eshop {
        return Eshop(eshopId, eshopLogoUrl, nickName)
    }
}
