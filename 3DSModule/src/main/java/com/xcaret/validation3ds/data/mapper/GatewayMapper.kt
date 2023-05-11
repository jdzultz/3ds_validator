package com.xcaret.validation3ds.data.mapper

import com.xcaret.validation3ds.data.models.GatewayEntity
import com.xcaret.validation3ds.domain.models.Gateway

class GatewayMapper (): Mapper<GatewayEntity, Gateway>{

    override fun mapFromEntity(type: GatewayEntity): Gateway {
        return Gateway(
            gatewayId = type.gatewayId,
            token = type.token,
            messageError = type.messageError,
        )
    }

    override fun mapToEntity(type: Gateway): GatewayEntity {
        return GatewayEntity(
            gatewayId = type.gatewayId,
            token = type.token,
            messageError = type.messageError
        )
    }

}