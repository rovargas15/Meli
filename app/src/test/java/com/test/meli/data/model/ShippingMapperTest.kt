package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ShippingMapperTest {

    @Test
    fun giveShippingDTOWhenMapperThenReturnShipping() {
        val shippingDTO = ShippingDTO(
            true,
            null,
            null,
            null,
            emptyList()
        )

        val result = shippingDTO.toDomainShipping()
        assertEquals(result.freeShipping, true)
        assertEquals(result.logisticType, null)
        assertEquals(result.mode, null)
        assertEquals(result.storePickUp, null)
        assertEquals(result.tags, emptyList<String>())
    }
}
