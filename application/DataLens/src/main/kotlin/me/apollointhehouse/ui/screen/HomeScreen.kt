package me.apollointhehouse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.map
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
import me.apollointhehouse.ui.components.DatalensPage
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
) = DatalensPage(
    name = "Home",
    topBar = {
        var fieldState by remember { mutableStateOf(TextFieldState(state.searchQuery)) }
        val scope = rememberCoroutineScope()

        scope.launch {
            search(
                query = fieldState.text.toString().replace("\n", ""),
                locator = locator,
                onStateChange = onStateChange,
                state = state
            )
        }

        // Input field for search
        OutlinedTextField(
            state = fieldState,
            placeholder = { Text(text = "⌕ Search Files...") },
            modifier = Modifier
                .onKeyEvent { keyEvent ->
                    val nativeEvent = keyEvent.nativeKeyEvent
                    val code = nativeEvent.awtKeyEvent.keyChar.code
                    val enterKey = 10
                    if (code != enterKey) return@onKeyEvent false
                    logger.debug { "enter pressed, performing search" }

                    fieldState = TextFieldState(fieldState.text.toString().replace("\n", ""))
                    true
                }
                .width(520.dp)
                .height(60.dp)
                .background(MaterialTheme.colors.background)
                .border(2.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.large)
        )
    },

    content = {
        val locatorState by locator.state.collectAsState()

        // Center the content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            displayState(locatorState)
            Spacer(modifier = Modifier.padding(16.dp))
            // Display search results
            Results(state.results)
        }
    }
)

// Display the current state of the locator (visibility of system status)
@Composable
private fun displayState(locatorState: LocatorState) = when (locatorState) {
    is LocatorState.Locating -> {
        Text(
            text = "Locating Files...",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.body2
        )
    }

    is LocatorState.Indexing -> {
        Text(
            text = "Preparing for search...",
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

private suspend fun search(
    query: String,
    locator: QueryLocator<String, Result<Set<Path>, LocatorError>>,
    onStateChange: (HomeState) -> Unit,
    state: HomeState
): Result<SearchResults, LocatorError> {
    var results: Result<SearchResults, LocatorError>

    // Run the search operation in the IO dispatcher to avoid blocking the main thread
    withContext(Dispatchers.IO) {

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

        onStateChange(state.copy(
            searchQuery = query.replace("\n", ""),
            results = results.getOrElse { SearchResults(emptySet()) },
        ))

        logger.info { "Elapsed: $elapsed" }
        logger.debug { "Results: $results" }
    }

    return results
}