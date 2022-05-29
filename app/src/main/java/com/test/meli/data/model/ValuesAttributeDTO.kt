package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValuesAttributeDTO(
    @Json(name = "name")
    val name: String?
)
