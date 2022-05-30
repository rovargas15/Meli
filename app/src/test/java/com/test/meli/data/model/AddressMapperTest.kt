package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AddressMapperTest {

    @Test
    fun giveAddressDTOWhenMapperThenReturnAddress() {
        val address = AddressDTO(
            cityName = null,
            stateId = null,
            stateName = null
        )

        val result = address.toDomainAddress()

        assertEquals(result.cityName, null)
        assertEquals(result.stateId, null)
        assertEquals(result.stateName, null)
    }
}
