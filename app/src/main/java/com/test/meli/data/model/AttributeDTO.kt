package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.Attribute

@JsonClass(generateAdapter = true)
data class AttributeDTO(
    @Json(name = "attribute_group_id")
    val attributeGroupId: String,
    @Json(name = "attribute_group_name")
    val attributeGroupName: String,
    @Json(name = "name")
    val name: String,
    val valueName: String?,
    @Json(name = "values")
    val values: List<ValuesAttributeDTO>,
) {
    fun toDomainAttribute(): Attribute {
        return Attribute(
            attributeGroupId,
            attributeGroupName,
            name,
            valueName,
            values.map {
                it.name ?: ""
            }
        )
    }
}
