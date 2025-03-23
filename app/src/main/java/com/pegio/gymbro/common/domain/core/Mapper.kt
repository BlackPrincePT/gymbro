package com.pegio.gymbro.common.domain.core

interface Mapper<T, Domain> {

    fun mapToDomain(data: T): Domain

    fun mapFromDomain(data: Domain): T
}