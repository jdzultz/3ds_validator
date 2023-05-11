package com.xcaret.validation3ds.remote.models

data class GatewayPayloadModel (
    val salesChannel: String,
    val currencyCode: String,
    val card: CardPayloadModel
)

data class CardPayloadModel (
    val cardNumber: String,
    val installmentsPlan: String,
    val bankId: String
)