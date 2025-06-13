package me.apollointhehouse.data.locator

sealed interface LocatorState {
    data object Idle : LocatorState
    data object Locating : LocatorState
    data object Indexing : LocatorState
}