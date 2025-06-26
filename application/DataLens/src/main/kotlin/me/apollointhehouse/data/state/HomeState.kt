package me.apollointhehouse.data.state

import me.apollointhehouse.data.SearchResults

data class HomeState(
    val searchQuery: String = "",
    val results: SearchResults = SearchResults(emptySet())
)
