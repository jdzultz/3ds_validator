package com.xcaret.validation3ds.data.source

import com.xcaret.validation3ds.data.repository.GatewayDataSource

class GatewayDataSourceFactory constructor(
    private val remoteDataSource: GatewayRemoteDataSource
) {

    open suspend fun getDataStore(): GatewayDataSource {
        return getRemoteDataSource()
    }

    fun getRemoteDataSource(): GatewayDataSource {
        return remoteDataSource
    }


}