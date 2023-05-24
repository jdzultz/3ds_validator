package com.xcaret.validation3ds.remote.mappers

import com.xcaret.validation3ds.data.models.GatewayEntity
import com.xcaret.validation3ds.remote.models.GatewayModel

class GatewayEntityMapper constructor(
    ) : EntityMapper<GatewayModel, GatewayEntity> {
        override fun mapFromModel(model: GatewayModel): GatewayEntity {
            return GatewayEntity(
                gatewayId = model.gatewayId,
                token = model.token,
                messageError = model.messageError,
            )
        }
    }