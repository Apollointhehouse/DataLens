package me.apollointhehouse.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.softwork.routingcompose.Router
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Home

data class Page(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun navbar(vararg pages: Page) {
    val router = Router.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .align(Alignment.BottomStart)
            .padding(16.dp),
    ) {
        for ((name, route) in pages) {
            Button(
                onClick = { router.navigate(route) },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Home,
                    contentDescription = name,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}