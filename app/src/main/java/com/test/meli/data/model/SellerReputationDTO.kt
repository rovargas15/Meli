package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.SellerReputation

@JsonClass(generateAdapter = true)
data class SellerReputationDTO(
    @Json(name = "level_id")
    val levelId: String?,
    @Json(name = "power_seller_status")
    val powerSellerStatus: String?
) {
    fun toDomainSellerReputation(): SellerReputation {
        return SellerReputation(levelId, powerSellerStatus)
    }
}
