package me.apollointhehouse.ui.components.scaffolds

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DesktopNavigationRail(
    header: @Composable () -> Unit = {},
    footer: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
    backgroundColor: Color = MaterialTheme.colors.onSurface,
    contentColor: Color = contentColorFor(backgroundColor),
    width: Dp = 56.dp,
    modifier: Modifier = Modifier
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        modifier = modifier.width(width).fillMaxHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight().padding(vertical = 8.dp)
        ) {
            // Header section
            header()

            // Main content section (navigation items)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                content = content
            )

            // Footer section
            footer()
        }
    }
}