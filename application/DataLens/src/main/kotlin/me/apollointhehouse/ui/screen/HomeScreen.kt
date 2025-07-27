package me.apollointhehouse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import app.softwork.routingcompose.Router
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.map
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.apollointhehouse.data.SearchResult
import me.apollointhehouse.data.SearchResults
import me.apollointhehouse.data.locator.LocatorError
import me.apollointhehouse.data.locator.LocatorState
import me.apollointhehouse.data.locator.QueryLocator
import me.apollointhehouse.data.state.HomeState
import me.apollointhehouse.ui.components.Results
import me.apollointhehouse.ui.utils.awtKeyEvent
import java.nio.file.Path
import kotlin.time.measureTime

private val logger = KotlinLogging.logger {}

@Composable
fun HomeScreen(
    state: HomeState,
    onStateChange: (HomeState) -> Unit,
    locator: QueryLocator<String, Result<Set<Path>, LocatorError>>,
) {
    val searchText = rememberTextFieldState(state.searchQuery)
    var locatorResults: Result<SearchResults, LocatorError> by remember { mutableStateOf(Ok(state.results)) }
    val locatorState by locator.state.collectAsState()

    val router = Router.current
    val scope = rememberCoroutineScope()

    // Observe changes in the search text and perform search
//    locatorResults = search(searchText, locatorResults, locator, onStateChange, state)

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
                Spacer(modifier = Modifier.height(16.dp))

                // Input field for search
                OutlinedTextField(
                    state = searchText,
                    placeholder = { Text(text = "Search...") },
                    modifier = Modifier
                        .onKeyEvent { keyEvent ->
                            val nativeEvent = keyEvent.nativeKeyEvent
                            val code = nativeEvent.awtKeyEvent.keyChar.code
                            val enterKey = 10

                            if (code != enterKey) return@onKeyEvent false

                            logger.debug { "enter pressed, performing search" }

                            scope.launch {
                                // Observe changes in the search text and perform search
                                locatorResults = search(searchText, locator, onStateChange, state)
                            }

                            true
                        }
                )
                when (locatorState) {
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

            // Navigation bar at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
            ) {
                Button(onClick = { router.navigate("/settings") }) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Cog,
                        contentDescription = "Settings",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private suspend fun search(
    searchText: TextFieldState,
    locator: QueryLocator<String, Result<Set<Path>, LocatorError>>,
    onStateChange: (HomeState) -> Unit,
    state: HomeState
): Result<SearchResults, LocatorError> {
    var results: Result<SearchResults, LocatorError>

    // Run the search operation in the IO dispatcher to avoid blocking the main thread
    withContext(Dispatchers.IO) {
        val query = searchText.text.toString()

        results = Ok(SearchResults(emptySet())) // Clear previous results
        logger.info { "Searching: $query" }

        // Measure the time taken for the search operation and log it (visibility of system status)
        val elapsed = measureTime {
            results = locator
                .locate(query = query)
                .map { paths ->
                    SearchResults(
                        results =
                            paths.map { path -> SearchResult(path) }.toSet()
                    )
                }
        }

        onStateChange(
            state.copy(
                searchQuery = query,
                results = results.getOrElse { SearchResults(emptySet()) }
            ))

        logger.info { "Elapsed: $elapsed" }
        logger.debug { "Results: $results" }
    }

    return results
}