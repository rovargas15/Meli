package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class EshopMapperTest {

    @Test
    fun giveAttributeDTOWhenMapperThenReturnAttribute() {
        val eshop = EshopDTO(
            eshopId = null,
            eshopLogoUrl = null,
            nickName = null
        )

        val result = eshop.toDomainEshop()

        assertEquals(result.eshopId, null)
        assertEquals(result.eshopLogoUrl, null)
        assertEquals(result.nickName, null)
    }
}
