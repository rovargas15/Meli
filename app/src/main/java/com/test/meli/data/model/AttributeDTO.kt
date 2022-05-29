package com.test.meli.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.meli.domain.model.Attribute

@JsonClass(generateAdapter = true)
data class AttributeDTO(
    @Json(name = "id")
    val id: String,
    @Json(name = "attribute_group_id")
    val attributeGroupId: String,
    @Json(name = "attribute_group_name")
    val attributeGroupName: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "source")
    val source: Long,
    @Json(name = "value_id")
    val valueId: String?,
    @Json(name = "value_name")
    val valueName: String?
) {
    fun toDomainAttribute(): Attribute {
        return Attribute(
            attributeGroupId,
            attributeGroupName,
            id,
            name,
            source,
            valueId,
            valueName
        )
    }
}
