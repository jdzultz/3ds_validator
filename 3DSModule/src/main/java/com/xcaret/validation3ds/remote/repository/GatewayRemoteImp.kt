package com.xcaret.validation3ds.remote.repository

import com.xcaret.validation3ds.data.models.GatewayEntity
import com.xcaret.validation3ds.data.models.GatewayPayloadEntity
import com.xcaret.validation3ds.data.repository.GatewayRemote
import com.xcaret.validation3ds.remote.api.GatewayService
import com.xcaret.validation3ds.remote.mappers.GatewayEntityMapper
import com.xcaret.validation3ds.remote.mappers.GatewayPayloadEntityMapper

class GatewayRemoteImp constructor(
    private val gatewayService: GatewayService,
    private val gatewayEntityMapper: GatewayEntityMapper,
    private val gatewayPayloadEntityMapper: GatewayPayloadEntityMapper
) : GatewayRemote {
    override suspend fun getGateway(payload: GatewayPayloadEntity): GatewayEntity {
        return gatewayEntityMapper.mapFromModel(gatewayService.getGateway(gatewayPayloadEntityMapper.mapFromModel(payload)))
    }


}