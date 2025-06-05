package me.apollointhehouse.ui.screen

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
import kotlinx.coroutines.flow.flowOn
import me.apollointhehouse.ui.components.Results
import me.apollointhehouse.data.locator.QueryLocator
import java.nio.file.Path
import kotlin.time.measureTime

private val logger = KotlinLogging.logger {}

@OptIn(FlowPreview::class)
@Composable
fun HomeScreen(
    locator: QueryLocator<String, Set<Path>>,
) {
    val searchText = rememberTextFieldState()

    var searchResults by remember { mutableStateOf<Set<Path>>(emptySet()) }

    // Observe changes in the search text and perform search
    LaunchedEffect(searchText.text) {
        snapshotFlow { searchText.text }
            .debounce(250)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO) // Perform search on IO dispatcher in order to avoid blocking the UI thread
            .collectLatest {
                val query = searchText.text.toString()
                // Measure the time taken for the search operation and log it (visibility of system status)
                val elapsed = measureTime {
                    searchResults = locator.locate(query)
                }
                logger.info { "Results: $searchResults" }
                logger.info { "Elapsed: $elapsed" }
            }
    }

    MaterialTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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

                Spacer(modifier = Modifier.padding(16.dp))

                // Display search results
                Results(searchResults)
            }

        }
    }
}