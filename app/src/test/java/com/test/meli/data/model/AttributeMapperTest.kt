package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AttributeMapperTest {

    @Test
    fun giveAttributeDTOWhenMapperThenReturnAttribute() {
        val values = ValuesAttributeDTO(
            name = "value"
        )
        val attribute = AttributeDTO(
            attributeGroupId = "attributeGroupId",
            attributeGroupName = "attributeGroupName",
            name = "name",
            valueName = "valueName",
            values = listOf(values)
        )

        val result = attribute.toDomainAttribute()

        assertEquals(result.attributeGroupId, "attributeGroupId")
        assertEquals(result.attributeGroupName, "attributeGroupName")
        assertEquals(result.name, "name")
        assertEquals(result.valueName, "valueName")
        assertEquals(result.values.first(), "value")
    }
}
