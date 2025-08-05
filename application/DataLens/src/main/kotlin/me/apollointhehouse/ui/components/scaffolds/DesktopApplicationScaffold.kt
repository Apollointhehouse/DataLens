package me.apollointhehouse.ui.components.scaffolds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DesktopApplicationScaffold(
    topBar: @Composable () -> Unit = {},
    navigationRail: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Top bar area
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(MaterialTheme.colors.primary)
                .align(Alignment.CenterHorizontally)
        ) {
            topBar()
        }

        // Main content row with navigation and content
        Row(modifier = Modifier.fillMaxSize()) {
            // Navigation rail (always visible)
            navigationRail()

            Box(
//                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
//                    .weight(1f)
                    .fillMaxHeight()
            ) {
                content()
            }
        }
    }
}