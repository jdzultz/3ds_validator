package com.xcaret.validation3ds.domain.models


data class GatewayPayload (
        val salesChannel: String,
        val currencyCode: String,
        val card: CardPayload
        )

data class CardPayload(
        val cardNumber: String,
        val installmentsPlan: String,
        val bankId: String
)