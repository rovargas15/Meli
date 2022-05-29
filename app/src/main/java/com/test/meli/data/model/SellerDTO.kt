package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.Seller

@JsonClass(generateAdapter = true)
data class SellerDTO(
    @Json(name = "id")
    val id: Int,
    @Json(name = "eshop")
    val eshop: EshopDTO?,
    @Json(name = "permalink")
    val permalink: String,
    @Json(name = "registration_date")
    val registrationDate: String?,
    @Json(name = "seller_reputation")
    val sellerReputation: SellerReputationDTO?,
    @Json(name = "tags")
    val tags: List<String>
) {

    fun toDomainSeller(): Seller {
        return Seller(
            id,
            eshop?.toDomainEshop(),
            permalink,
            registrationDate,
            sellerReputation?.toDomainSellerReputation(),
            tags
        )
    }
}
