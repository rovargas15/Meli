package com.test.meli.data.model

import com.squareup.moshi.Json
import com.test.meli.domain.model.Seller

data class SellerDTO(
    @Json(name = "id")
    val id: Int,
    @Json(name = "car_dealer")
    val eshopDTO: EshopDTO,
    @Json(name = "permalink")
    val permalink: String,
    @Json(name = "registration_date")
    val registrationDate: String,
    @Json(name = "seller_reputation")
    val sellerReputation: SellerReputationDTO,
    @Json(name = "tags")
    val tags: List<String>
) {

    fun toDomainSeller(): Seller {
        return Seller(
            id,
            eshopDTO.toDomainEshop(),
            permalink,
            registrationDate,
            sellerReputation.toDomainSellerReputation(),
            tags
        )
    }
}
