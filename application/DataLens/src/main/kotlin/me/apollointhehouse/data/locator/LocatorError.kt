package me.apollointhehouse.data.locator

sealed interface LocatorError {
    data object NotFound : LocatorError
    data object InvalidQuery : LocatorError
    data object IndexingFailed : LocatorError
    data class UnknownError(val message: String) : LocatorError
}