package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AddressMapperTest {

    @Test
    fun giveAddressDTOWhenMapperThenReturnAddress() {
        val address = AddressDTO(
            cityName = "cityName",
            stateId = "stateId",
            stateName = "stateName"
        )

        val result = address.toDomainAddress()

        assertEquals(result.cityName, "cityName")
        assertEquals(result.stateId, "stateId")
        assertEquals(result.stateName, "stateName")
    }
}
