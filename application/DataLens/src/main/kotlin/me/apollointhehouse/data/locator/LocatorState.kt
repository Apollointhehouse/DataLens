package me.apollointhehouse.data.locator

import kotlinx.coroutines.Job

sealed interface LocatorState {
    data object Idle : LocatorState
    data object Locating : LocatorState
    data class Indexing(val job: Job) : LocatorState
}