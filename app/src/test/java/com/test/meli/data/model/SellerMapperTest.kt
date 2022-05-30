package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class SellerMapperTest {

    @Test
    fun giveEshopDTOWhenMapperThenReturnSeller() {
        val sellerDTO = SellerDTO(
            1,
            null,
            "permalink",
            "registrationDate",
            null,
            emptyList()
        )

        val result = sellerDTO.toDomainSeller()

        assertEquals(result.id, 1)
        assertEquals(result.eshop, null)
        assertEquals(result.permalink, "permalink")
        assertEquals(result.registrationDate, "registrationDate")
        assertEquals(result.sellerReputation, null)
        assertEquals(result.tags, emptyList<String>())
    }
}
