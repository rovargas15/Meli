package com.test.meli.domain.model

data class Attribute(
    val attributeGroupId: String,
    val attributeGroupName: String,
    val id: String,
    val name: String,
    val source: Long,
    val valueId: String?,
    val valueName: String?
)
