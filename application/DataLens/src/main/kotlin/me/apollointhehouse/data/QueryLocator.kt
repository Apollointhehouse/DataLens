package me.apollointhehouse.data

interface QueryLocator<T : Any, R : Collection<*>> {
    suspend fun locate(query: T): R
}
