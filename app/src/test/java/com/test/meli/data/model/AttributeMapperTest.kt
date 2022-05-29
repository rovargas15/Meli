package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AttributeMapperTest {

    @Test
    fun giveAttributeDTOWhenMapperThenReturnAttribute() {
        val attribute = AttributeDTO(
            attributeGroupId = "attributeGroupId",
            attributeGroupName = "attributeGroupName",
            id = "id",
            name = "name",
            source = 1,
            valueId = "valueId",
            valueName = "valueName"
        )

        val result = attribute.toDomainAttribute()

        assertEquals(result.attributeGroupId, "attributeGroupId")
        assertEquals(result.attributeGroupName, "attributeGroupName")
        assertEquals(result.id, "id")
        assertEquals(result.name, "name")
        assertEquals(result.source, 1)
        assertEquals(result.valueId, "valueId")
        assertEquals(result.valueName, "valueName")
    }
}
