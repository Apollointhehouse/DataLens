package me.apollointhehouse.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.SearchResults
import me.apollointhehouse.data.locator.LocatorError
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

@Composable
fun Results(locatorResults: Result<SearchResults, LocatorError>) {
    LazyColumn (
        modifier = Modifier
            .padding(16.dp)
            .height(600.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.onSecondary)
    ) {
        locatorResults.fold(
            success = { createResultCards(it) },
            failure = {
                // Display an error message if the search operation failed
                item {
                    Text(
                        text = "Error: $it.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        )
    }
}

private fun LazyListScope.createResultCards(results: SearchResults) {
    for (path in results.paths) {
        item {
            // Create a Card for each search result item
            Card(
                modifier = Modifier
                    .width(700.dp)
                    .padding(8.dp)
            ) {
                // Inside the Card, create a Row to display the file name and an "Open" button
                Row {
                    Text(
                        text = path.fileName?.toString() ?: path.toString(),
                        modifier = Modifier
                            .padding(16.dp)
                            .width(300.dp),
                    )

                    Spacer(Modifier.weight(1f, true))

                    Button(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(120.dp),
                        onClick = {
                            open(path.parent)
                        },
                    ) {
                        Text(text = "Open Location")
                    }
                    Spacer(Modifier.width(4.dp))
                    Button(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(120.dp),
                        onClick = {
                            open(path)
                        },
                    ) {
                        Text(text = "Open")
                    }
                }
            }
        }
    }
}

// Open the file in the system's default application
private fun open(path: Path) {
    if (!Desktop.isDesktopSupported()) {
        logger.error { "Failed to open path!" }
    }

    try {
        Desktop.getDesktop().open(path.toFile())
    } catch (_: IOException) {
        logger.error { "Failed to open link!" }
    }
}