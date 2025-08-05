package me.apollointhehouse.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.SearchResult
import me.apollointhehouse.data.SearchResults
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

@Composable
fun Results(locatorResults: SearchResults) {
    logger.debug { "Results Composable called with locatorResults: $locatorResults" }

    val results: SnapshotStateList<SearchResult> = locatorResults.results.toMutableStateList()

    logger.debug { "State Results: $results" }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .height(600.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.background)
    ) {
        logger.debug { "Creating result card!" }
        createResultCards(results)
    }

}

private fun LazyListScope.createResultCards(results: SnapshotStateList<SearchResult>) {
    logger.debug { "Results Size: ${results.size}" }

    items(results.size) { i ->
        val result = results[i]

        ResultCard(result) {

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ResultCard(result: SearchResult, onClick: () -> Unit) {
    val path = result.path

    Card(
        elevation = 4.dp,
        modifier = Modifier
            .width(700.dp)
            .padding(8.dp)
            .clickable {  }
            .onClick(onClick = onClick),
    ) {
        // Inside the Card, create a Row to display the file name and an "Open" button
        Row {
            Text(
                text = path.fileName?.toString() ?: result.toString(),
                modifier = Modifier
                    .padding(16.dp)
                    .width(300.dp)

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