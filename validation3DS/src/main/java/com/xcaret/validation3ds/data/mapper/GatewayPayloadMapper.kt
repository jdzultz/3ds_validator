package com.xcaret.validation3ds.data.mapper

import com.xcaret.validation3ds.data.models.CardPayloadEntity
import com.xcaret.validation3ds.data.models.GatewayPayloadEntity
import com.xcaret.validation3ds.domain.models.CardPayload
import com.xcaret.validation3ds.domain.models.GatewayPayload

class GatewayPayloadMapper (): Mapper<GatewayPayloadEntity, GatewayPayload>{

    override fun mapFromEntity(type: GatewayPayloadEntity): GatewayPayload {
        return GatewayPayload(
            salesChannel = type.salesChannel,
            currencyCode = type.currencyCode,
            card = CardPayload(
                cardNumber = type.card.cardNumber,
                installmentsPlan = type.card.installmentsPlan,
                bankId = type.card.bankId
            )
        )
    }

    override fun mapToEntity(type: GatewayPayload): GatewayPayloadEntity {
        return GatewayPayloadEntity(
            salesChannel = type.salesChannel,
            currencyCode = type.currencyCode,
            card = CardPayloadEntity(
                cardNumber = type.card.cardNumber,
                installmentsPlan = type.card.installmentsPlan,
                bankId = type.card.bankId
            )
        )
    }

}