package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class EshopMapperTest {

    @Test
    fun giveAttributeDTOWhenMapperThenReturnAttribute() {
        val eshop = EshopDTO(
            eshopId = 1,
            eshopLogoUrl = "url",
            nickName = "name"
        )

        val result = eshop.toDomainEshop()

        assertEquals(result.eshopId, 1)
        assertEquals(result.eshopLogoUrl, "url")
        assertEquals(result.nickName, "name")
    }
}
