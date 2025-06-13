package me.apollointhehouse.data.locator.impl

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.apollointhehouse.data.locator.LocatorError
import me.apollointhehouse.data.locator.LocatorState
import me.apollointhehouse.data.locator.QueryLocator
import java.nio.file.Path

/**
 * A QueryLocator implementation that simulates locating "Foo" items.
 * This is for mock purposes and does not perform any real search.
 */
class TestLocator : QueryLocator<String, Result<Set<Path>, LocatorError>> {
    private var _state = MutableStateFlow<LocatorState>(LocatorState.Idle)  // Initial state of the locator
    override var state = _state.asStateFlow() // Initial state of the locator

    override suspend fun locate(query: String): Result<Set<Path>, LocatorError> {
        // Simulate a search operation
        _state.value = LocatorState.Locating // Set state to Locating when performing a search
        delay(250) // Simulate a delay of 250ms
        _state.value = LocatorState.Idle // Set state to Locating when performing a search

        return Ok(setOf(Path.of("Foo1"), Path.of("Foo2"), Path.of("Foo3")))
    }
}