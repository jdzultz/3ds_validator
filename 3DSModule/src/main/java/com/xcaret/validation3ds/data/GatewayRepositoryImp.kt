package com.xcaret.validation3ds.data

import com.xcaret.validation3ds.data.mapper.GatewayMapper
import com.xcaret.validation3ds.data.mapper.GatewayPayloadMapper
import com.xcaret.validation3ds.data.source.GatewayDataSourceFactory
import com.xcaret.validation3ds.domain.models.Gateway
import com.xcaret.validation3ds.domain.models.GatewayPayload
import com.xcaret.validation3ds.domain.repository.GatewayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GatewayRepositoryImp constructor(
    private val dataSourceFactory: GatewayDataSourceFactory,
    private val gatewayMapper: GatewayMapper,
    private val gatewayPayloadMapper: GatewayPayloadMapper
) : GatewayRepository {

    override suspend fun getGateway(payload: GatewayPayload): Flow<Gateway> = flow {
        var gateway = dataSourceFactory.getDataStore().getGateway(gatewayPayloadMapper.mapToEntity(payload))
        emit(
            gatewayMapper.mapFromEntity(gateway)
        )
    }

}