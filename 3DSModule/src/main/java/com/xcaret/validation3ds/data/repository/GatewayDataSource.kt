package com.xcaret.validation3ds.data.repository

import com.xcaret.validation3ds.data.models.GatewayEntity
import com.xcaret.validation3ds.data.models.GatewayPayloadEntity

interface GatewayDataSource {
    suspend fun getGateway(payload: GatewayPayloadEntity): GatewayEntity
}