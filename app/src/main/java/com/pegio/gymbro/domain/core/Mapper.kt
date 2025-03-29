package com.pegio.gymbro.domain.core

interface Mapper<T, Domain> : ToDomainMapper<T, Domain>, FromDomainMapper<T, Domain>

interface ToDomainMapper<T, Domain>{
    fun mapToDomain(data: T): Domain
}

interface FromDomainMapper<T, Domain>{
    fun mapFromDomain(data: Domain): T
}