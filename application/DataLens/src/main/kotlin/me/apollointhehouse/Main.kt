package me.apollointhehouse

import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.softwork.routingcompose.DesktopRouter
import app.softwork.routingcompose.Router
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Eye
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.db.initDatabase
import me.apollointhehouse.data.locator.impl.NameLocator
import me.apollointhehouse.ui.screen.HomeScreen
import me.apollointhehouse.ui.screen.SettingsScreen
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

fun main() = application {
    logger.info { "Starting main application..." }

    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info { "Shutting Down!" }
    })

    logger.info { "Initializing Database..." }
    initDatabase()

    logger.info { "DataLens Initialised!" }


    val basePaths = listOf(
        Path.of("C:\\"),
//        Path.of(System.getProperty("user.home")),
//        Path.of("\\\\internal.rotorualakes.school.nz\\Users\\Home\\")
    )

    println("Base paths: $basePaths")

    Window(
        title = "DataLens",
        icon = rememberVectorPainter(FontAwesomeIcons.Regular.Eye),
        onCloseRequest = ::exitApplication
    ) {
        DesktopRouter("/") {
            route("/") {
                HomeScreen(NameLocator(basePaths))
            }

            route("/settings") {
                SettingsScreen()
            }

            noMatch {
                Router.current.navigate(to = "/")
            }
        }


    }
}