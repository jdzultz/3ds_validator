package com.xcaret.validation3ds.data.mapper

interface Mapper<E, D> {

    fun mapFromEntity(type: E): D

    fun mapToEntity(type: D): E
}