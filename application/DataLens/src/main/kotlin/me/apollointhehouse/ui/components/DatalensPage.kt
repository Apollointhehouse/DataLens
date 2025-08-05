package me.apollointhehouse.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.softwork.routingcompose.Router
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.Home
import me.apollointhehouse.ui.components.scaffolds.DesktopApplicationScaffold
import me.apollointhehouse.ui.components.scaffolds.DesktopNavigationRail

@Composable
fun DatalensPage(
    name: String,
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) = AppTheme {
    val router = Router.current

    DesktopApplicationScaffold(
        topBar = {
            topBar()
        },
        navigationRail = {
            DesktopNavigationRail(
                header = {
                    Spacer(modifier = Modifier.height(16.dp))
                },
                content = {
                    val nav = listOf(
                        Page("Home", "/", FontAwesomeIcons.Solid.Home),
                        Page("Settings", "/settings", FontAwesomeIcons.Solid.Cog),
                    )

                    for (page in nav) {
                        NavRailItem(
                            icon = page.icon,
                            isSelected = page.name == name,
                            onClick = { router.navigate(page.route) }
                        )
                    }
                },
                footer = {
                    Spacer(modifier = Modifier.height(16.dp))
                },
                backgroundColor = MaterialTheme.colors.primarySurface,
                width = 56.dp
            )
        }
    ) {
        content()
    }
}