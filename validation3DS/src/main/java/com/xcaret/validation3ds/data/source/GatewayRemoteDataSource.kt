package com.xcaret.validation3ds.data.source

import com.xcaret.validation3ds.data.models.GatewayEntity
import com.xcaret.validation3ds.data.models.GatewayPayloadEntity
import com.xcaret.validation3ds.data.repository.GatewayDataSource
import com.xcaret.validation3ds.data.repository.GatewayRemote

class GatewayRemoteDataSource constructor(
    private val dataSourceRemote: GatewayRemote
) : GatewayDataSource {
    override suspend fun getGateway(payload: GatewayPayloadEntity): GatewayEntity {
        return dataSourceRemote.getGateway(payload)
    }
}