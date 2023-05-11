package com.xcaret.validation3ds.data.models

data class GatewayPayloadEntity (
    val salesChannel: String,
    val currencyCode: String,
    val card: CardPayloadEntity
)

data class CardPayloadEntity(
    val cardNumber: String,
    val installmentsPlan: String,
    val bankId: String
)