package me.apollointhehouse.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun NavRailItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primarySurface else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "Background Color Animation"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.background.copy(alpha = 0.8f),
        animationSpec = tween(durationMillis = 300),
        label = "Content Color Animation"
    )

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor
            )
        }
    }
}
