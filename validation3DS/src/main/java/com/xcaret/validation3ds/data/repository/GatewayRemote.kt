package com.xcaret.validation3ds.data.repository

import com.xcaret.validation3ds.data.models.GatewayEntity
import com.xcaret.validation3ds.data.models.GatewayPayloadEntity

interface GatewayRemote {
    suspend fun getGateway(payload: GatewayPayloadEntity): GatewayEntity
}