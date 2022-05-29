package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.Address

@JsonClass(generateAdapter = true)
data class AddressDTO(
    @Json(name = "city_name")
    val cityName: String?,
    @Json(name = "state_id")
    val stateId: String?,
    @Json(name = "state_name")
    val stateName: String?
) {
    fun toDomainAddress(): Address {
        return Address(
            cityName, stateId, stateName
        )
    }
}
