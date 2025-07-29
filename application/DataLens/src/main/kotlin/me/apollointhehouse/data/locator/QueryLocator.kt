package me.apollointhehouse.data.locator

import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.StateFlow

// Generic interface for locating queries (useful if I ever want to implement different types of locators in the future)
interface QueryLocator<T : Any, R : Result<Collection<*>, LocatorError>> {
    suspend fun locate(query: T): R

    val state: StateFlow<LocatorState>
}