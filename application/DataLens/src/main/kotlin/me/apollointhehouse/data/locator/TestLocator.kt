package me.apollointhehouse.data.locator

import kotlinx.coroutines.delay
import java.nio.file.Path

/**
 * A QueryLocator implementation that simulates locating "Foo" items.
 * This is for mock purposes and does not perform any real search.
 */
class TestLocator : QueryLocator<String, Set<Path>> {
    override suspend fun locate(query: String): Set<Path> {
        // Simulate a search operation
        delay(250) // Simulate a delay of 250ms

        return setOf(Path.of("Foo1"), Path.of("Foo2"), Path.of("Foo3"))
    }
}