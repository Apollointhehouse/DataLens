package me.apollointhehouse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Home
import me.apollointhehouse.data.Match
import me.apollointhehouse.ui.components.DatalensPage
import me.apollointhehouse.ui.components.Page
import me.apollointhehouse.ui.components.navbar

@Composable
fun SettingsScreen(
    match: Match,
    onMatchChange: (Match) -> Unit,
) = DatalensPage(
    Page("Home", "/", FontAwesomeIcons.Solid.Home)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(38.dp)
    ) {
        relevanceSlider(match, onMatchChange)
    }
}

@Composable
private fun relevanceSlider(
    match: Match,
    onMatchChange: (Match) -> Unit,
) {
    Card {
        Text("Search Relevance", modifier = Modifier.padding(8.dp))
        TabRow(
            selectedTabIndex = match.ordinal,
            modifier = Modifier.width(550.dp)
        ) {
            Match.entries.forEachIndexed { index, option ->
                Tab(
                    selected = match.ordinal == index,
                    onClick = { onMatchChange(option) },
                    text = {
                        val name = option.name
                            .lowercase()
                            .replace("_", " ")
                            .split(" ")
                            .joinToString(" ") {
                                it.replaceFirstChar { char -> char.uppercase() }
                            }
                        Text(name)
                    },
                )
            }
        }
    }
}