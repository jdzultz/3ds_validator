package com.xcaret.validation3ds.remote.models
import com.squareup.moshi.Json

data class GatewayModel (

    @field:Json(name = "gatewayId")
    val gatewayId: Int,

    @field:Json(name = "token")
    val token: String,

    @field:Json(name = "messageError")
    val messageError: String
)