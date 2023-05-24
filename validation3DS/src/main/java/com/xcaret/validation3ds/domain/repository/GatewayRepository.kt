package com.xcaret.validation3ds.domain.repository;

import com.xcaret.validation3ds.domain.models.Gateway;
import com.xcaret.validation3ds.domain.models.GatewayPayload

import kotlinx.coroutines.flow.Flow;

interface GatewayRepository {
        // Remote and cache
 suspend fun getGateway(payload: GatewayPayload): Flow<Gateway>
}
