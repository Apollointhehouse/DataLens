package me.apollointhehouse

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import info.debatty.java.stringsimilarity.Jaccard
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.NameLocator
import me.apollointhehouse.screen.HomeScreen
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

private val usr = Path.of(System.getProperty("user.home"))

fun main() = application {
    logger.info { "DataLens Initialised!" }

    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info { "Shutting Down!" }
    })

    Window(onCloseRequest = ::exitApplication) {
        HomeScreen(NameLocator(usr, Jaccard()))
    }
}