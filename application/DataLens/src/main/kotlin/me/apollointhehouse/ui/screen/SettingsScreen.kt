package me.apollointhehouse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.softwork.routingcompose.Router
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Home
import me.apollointhehouse.data.Match

@Composable
fun SettingsScreen(
    match: Match,
    onMatchChange: (Match) -> Unit,
) {
    val router = Router.current

    MaterialTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colors.background)
        ) {
            Column {
                Card {
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
//                                enabled = false
                            )
                        }
                    }
                }

            }

            // Navigation bar at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
            ) {
                Button(onClick = { router.navigate("/") }) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}