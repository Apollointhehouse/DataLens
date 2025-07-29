package me.apollointhehouse.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun DatalensPage(
    vararg nav: Page,
    content: @Composable () -> Unit
) = MaterialTheme {
    Scaffold(
        bottomBar = {
            // Navigation bar at the bottom
            navbar(*nav)
        },
    ) {
        content()
    }
}