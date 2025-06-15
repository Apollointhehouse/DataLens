package me.apollointhehouse.data.locator

import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.StateFlow

interface QueryLocator<T : Any, R : Result<Collection<*>, LocatorError>> {
    suspend fun locate(query: T): R

    val state: StateFlow<LocatorState>
}