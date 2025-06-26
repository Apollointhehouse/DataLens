package me.apollointhehouse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import me.apollointhehouse.data.SearchResult
import me.apollointhehouse.data.SearchResults
import me.apollointhehouse.data.locator.LocatorError
import me.apollointhehouse.data.locator.LocatorState
import me.apollointhehouse.data.locator.QueryLocator
import me.apollointhehouse.data.state.HomeState
import me.apollointhehouse.ui.components.Results
import java.nio.file.Path
import kotlin.time.measureTime

private val logger = KotlinLogging.logger {}

@OptIn(FlowPreview::class)
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
                            .locate(query = query)
                            .map { paths ->
                                SearchResults(results =
                                    paths.map { path -> SearchResult(path) }.toSet()
                                )
                            }
                    }

                    onStateChange(state.copy(
                        searchQuery = query,
                        results = locatorResults.getOrElse { SearchResults(emptySet()) }
                    ))

                    logger.info { "Elapsed: $elapsed" }
                    logger.debug { "Results: $locatorResults" }
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
                Spacer(modifier = Modifier.height(16.dp))

                // Input field for search
                OutlinedTextField(
                    state = searchText,
                    placeholder = { Text(text = "Search...") },
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