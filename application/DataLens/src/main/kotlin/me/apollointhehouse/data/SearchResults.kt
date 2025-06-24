package me.apollointhehouse.data

import java.nio.file.Path

data class SearchResults(val results: Set<SearchResult>)
data class SearchResult(val path: Path)
