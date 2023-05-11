package com.xcaret.validation3ds.remote.mappers

import com.xcaret.validation3ds.data.models.GatewayPayloadEntity
import com.xcaret.validation3ds.remote.models.CardPayloadModel
import com.xcaret.validation3ds.remote.models.GatewayPayloadModel

class GatewayPayloadEntityMapper constructor(
    ) : EntityMapper<GatewayPayloadEntity,GatewayPayloadModel> {
        override fun mapFromModel(model: GatewayPayloadEntity): GatewayPayloadModel {
            return GatewayPayloadModel(
                salesChannel = model.salesChannel,
                currencyCode = model.currencyCode,
                card = CardPayloadModel(
                    cardNumber = model.card.cardNumber,
                    installmentsPlan = model.card.installmentsPlan,
                    bankId = model.card.bankId
                )
            )
        }
    }