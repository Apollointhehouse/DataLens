package me.apollointhehouse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import me.apollointhehouse.data.SearchResults
import me.apollointhehouse.data.locator.QueryLocator
import me.apollointhehouse.ui.components.Results
import java.nio.file.Path
import kotlin.time.measureTime

private val logger = KotlinLogging.logger {}

@OptIn(FlowPreview::class)
@Composable
fun HomeScreen(
    locator: QueryLocator<String, Set<Path>>,
) {
    val searchText = rememberTextFieldState()
    var searchResults by remember { mutableStateOf(SearchResults(emptySet())) }
    var isLocating by remember { mutableStateOf(false) }
    var isIndexing by remember { mutableStateOf(true) }

    // Observe changes in the search text and perform search
    LaunchedEffect(searchText.text) {
        snapshotFlow { searchText.text }
            .debounce(700) // Debounce to avoid too many search operations
            .distinctUntilChanged()
            .collectLatest {
                // Run the search operation in the IO dispatcher to avoid blocking the main thread
                withContext(Dispatchers.IO) {
                    val query = it.toString()

                    searchResults = SearchResults(emptySet()) // Clear previous results
                    logger.info { "Searching: $query" }
                    isLocating = true // Set locating state to true

                    // Measure the time taken for the search operation and log it (visibility of system status)
                    val elapsed = measureTime {
                        searchResults = SearchResults(locator.locate(query))
                    }
//                    logger.info { "Results: $searchResults" }
                    logger.info { "Elapsed: $elapsed" }
                    isLocating = false // Set locating state to false
                    isIndexing = false
                }
            }
    }

    MaterialTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colors.background)
        ) {
            // Center the content in the Box and list the components vertically
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                // Input field for search
                TextField(
                    state = searchText,
                    placeholder = { Text(text = "Search...") },
                )
                if (isLocating) {
                    // Show a loading indicator while searching
                    Text(
                        text = if (!isIndexing) "Locating..." else "Indexing file system...",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.body2
                    )
                } else {
                    // Show the number of results found
                    Text(
                        text = "${searchResults.results.size} results found:",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.padding(16.dp))

                // Display search results
                Results(searchResults)
            }

        }
    }
}