package me.apollointhehouse.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

@Composable
fun Results(searchResults: Set<Path>) {
    Column {
        for (item in searchResults) {
            // Create a Card for each search result item
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .padding(8.dp)
            ) {
                // Inside the Card, create a Row to display the file name and an "Open" button
                Row {
                    Text(
                        text = item.fileName.toString(),
                        modifier = Modifier
                            .padding(16.dp)
                            .width(300.dp),
                    )
                    Spacer(Modifier.width(16.dp))
                    Button(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(180.dp),
                        onClick = {
                            open(item)
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