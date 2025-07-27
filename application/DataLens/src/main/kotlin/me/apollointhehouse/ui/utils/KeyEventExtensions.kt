package me.apollointhehouse.ui.utils

import androidx.compose.ui.input.key.NativeKeyEvent
import java.awt.event.KeyEvent

// Dirty reflection hacks
internal val NativeKeyEvent.awtKeyEvent: KeyEvent
    get() {
        val klass = this::class.java
        val field = klass.getDeclaredField("nativeEvent")
        field.isAccessible = true

        return field.get(this) as KeyEvent
    }