package com.xcaret.validation3ds.domain.interactor

import com.xcaret.validation3ds.domain.models.Gateway
import com.xcaret.validation3ds.domain.models.GatewayPayload
import com.xcaret.validation3ds.domain.repository.GatewayRepository
import kotlinx.coroutines.flow.Flow

typealias GetGatewayBaseUseCase = BaseUseCase<GatewayPayload, Flow<Gateway>>
class GetGatewayUseCase constructor(
    private val gatewayRepository: GatewayRepository
): GetGatewayBaseUseCase{
        override suspend operator fun invoke(params: GatewayPayload) = gatewayRepository.getGateway(params)
}