package me.apollointhehouse.data.locator

interface QueryLocator<T : Any, R : Collection<*>> {
    suspend fun locate(query: T): R
}