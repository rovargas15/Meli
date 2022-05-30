package com.test.meli.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ResultMapperTest {

    @Test
    fun giveResultDTOWhenMapperThenReturnResult() {
        val eshopDTO = EshopDTO(1, "store", "url")
        val sellerReputationDTO = SellerReputationDTO("id", "powerSellerStatus")
        val sellerDTO = SellerDTO(
            1,
            eshopDTO,
            "permalink",
            "registrationDate",
            sellerReputationDTO,
            emptyList()
        )

        val address = AddressDTO(
            cityName = "cityName",
            stateId = "stateId",
            stateName = "stateName"
        )

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

        val shippingDTO = ShippingDTO(
            true,
            "logisticType",
            "mode",
            true,
            emptyList()
        )

        val result = ResultDTO(
            "id",
            "title",
            123.1,
            sellerDTO,
            "condition",
            true,
            address,
            listOf(attribute),
            1,
            shippingDTO,
            "thumbnail"
        )

        val map = result.toResult()
        assertEquals(map.id, "id")
        assertEquals(map.title, "title")
        assert(map.price == 123.1)
        assertEquals(result.seller.id, 1)
        assertEquals(result.seller.eshop?.eshopId, 1)
        assertEquals(result.seller.eshop?.nickName, "store")
        assertEquals(result.seller.eshop?.eshopLogoUrl, "url")
        assertEquals(result.seller.permalink, "permalink")
        assertEquals(result.seller.registrationDate, "registrationDate")
        assertEquals(result.seller.sellerReputation?.levelId, "id")
        assertEquals(result.seller.sellerReputation?.powerSellerStatus, "powerSellerStatus")
        assertEquals(result.seller.tags, emptyList<String>())
        assertEquals(result.condition, "condition")
        assertEquals(result.acceptsMercadoPago, true)
        assertEquals(result.address?.cityName, "cityName")
        assertEquals(result.address?.stateId, "stateId")
        assertEquals(result.address?.stateName, "stateName")
        assertEquals(result.shipping?.freeShipping, true)
        assertEquals(result.shipping?.logisticType, "logisticType")
        assertEquals(result.shipping?.mode, "mode")
        assertEquals(result.shipping?.storePickUp, true)
        assertEquals(result.shipping?.tags, emptyList<String>())
        assertEquals(result.availableQuantity, 1)
        assertEquals(result.thumbnail, "thumbnail")
    }
}
