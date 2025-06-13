package me.apollointhehouse.data

import com.github.michaelbull.result.Result
import me.apollointhehouse.data.locator.LocatorError
import java.nio.file.Path

data class SearchResults(val results: Result<Set<Path>, LocatorError>)
