package com.xcaret.validation3ds.remote.mappers

interface EntityMapper<M, E> {

    fun mapFromModel(model: M): E
}
