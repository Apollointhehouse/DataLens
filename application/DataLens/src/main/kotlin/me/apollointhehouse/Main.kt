package me.apollointhehouse

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import info.debatty.java.stringsimilarity.Jaccard
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.locator.NameLocator
import me.apollointhehouse.ui.screen.HomeScreen
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

fun main() = application {
    logger.info { "DataLens Initialised!" }

    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info { "Shutting Down!" }
    })

    val basePaths = listOf(
//        Path.of("C:\\"),
        Path.of(System.getProperty("user.home")),
        Path.of("\\\\internal.rotorualakes.school.nz\\Users\\Home\\")
    )

    println("Base paths: $basePaths")

    Window(onCloseRequest = ::exitApplication) {
        // Uses dependency injection to provide the NameLocator instance to the HomeScreen (allows for easy testing/swapping of components)
        HomeScreen(NameLocator(basePaths, Jaccard()))
    }
}