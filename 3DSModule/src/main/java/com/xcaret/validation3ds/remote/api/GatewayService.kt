package com.xcaret.validation3ds.remote.api

import com.xcaret.validation3ds.remote.models.GatewayModel
import com.xcaret.validation3ds.remote.models.GatewayPayloadModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
interface GatewayService {
        @Headers("Content-Type: application/json")
        @POST("GetGatewayPayment")
        suspend fun getGateway(@Body payload: GatewayPayloadModel): GatewayModel
}