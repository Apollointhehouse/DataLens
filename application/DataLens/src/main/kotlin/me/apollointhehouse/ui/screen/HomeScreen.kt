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
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import me.apollointhehouse.data.SearchResults
import me.apollointhehouse.data.locator.LocatorError
import me.apollointhehouse.data.locator.LocatorState
import me.apollointhehouse.data.locator.QueryLocator
import me.apollointhehouse.ui.components.Results
import java.nio.file.Path
import kotlin.time.measureTime

private val logger = KotlinLogging.logger {}

@OptIn(FlowPreview::class)
@Composable
fun HomeScreen(
    locator: QueryLocator<String, Result<Set<Path>, LocatorError>>,
) {
    val searchText = rememberTextFieldState()
    var locatorResults: Result<SearchResults, LocatorError> by remember { mutableStateOf(Err(LocatorError.NoResults)) }
    val state = locator.state.collectAsState().value

    // Observe changes in the search text and perform search
    LaunchedEffect(searchText.text) {
        snapshotFlow { searchText.text }
            .debounce(700) // Debounce to avoid too many search operations
            .distinctUntilChanged()
            .collectLatest {
                // Run the search operation in the IO dispatcher to avoid blocking the main thread
                withContext(Dispatchers.IO) {
                    val query = it.toString()

                    locatorResults = Ok(SearchResults(emptySet())) // Clear previous results
                    logger.info { "Searching: $query" }

                    // Measure the time taken for the search operation and log it (visibility of system status)
                    val elapsed = measureTime {
                        locatorResults = locator
                            .locate(query)
                            .map { value -> SearchResults(value) }
                    }
//                    logger.info { "Results: $searchResults" }
                    logger.info { "Elapsed: $elapsed" }
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

                when (state) {
                    is LocatorState.Locating -> {
                        Text(
                            text = "Locating...",
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                    is LocatorState.Indexing -> {
                        Text(
                            text = "Indexing file system...",
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                    is LocatorState.Idle -> {
                        Text(
                            text = "Ready to search!",
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))

                // Display search results
                Results(locatorResults)
            }

        }
    }
}