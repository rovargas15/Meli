package com.test.meli.domain.model

data class Attribute(
    val attributeGroupId: String,
    val attributeGroupName: String,
    val name: String,
    val valueName: String?,
    val values: List<String>
)
