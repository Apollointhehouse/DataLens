package me.apollointhehouse

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.softwork.routingcompose.DesktopRouter
import app.softwork.routingcompose.Router
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Eye
import info.debatty.java.stringsimilarity.Jaccard
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.Match
import me.apollointhehouse.data.db.initDatabase
import me.apollointhehouse.data.db.repository.IndexRepo
import me.apollointhehouse.data.locator.impl.NameLocator
import me.apollointhehouse.data.state.HomeState
import me.apollointhehouse.ui.screen.HomeScreen
import me.apollointhehouse.ui.screen.SettingsScreen
import java.nio.file.Path
import javax.swing.filechooser.FileSystemView
import kotlin.io.path.exists


private val logger = KotlinLogging.logger {}
private val usrDir = Path.of(System.getenv("USERPROFILE"))
val applicationDir: Path = Path.of("$usrDir/DataLens").toAbsolutePath()

fun main() = application {
    logger.info { "Starting main application..." }

    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info { "Shutting Down!" }
    })

    logger.info { "Initializing Database..." }
    val db = initDatabase()

    val view = FileSystemView.getFileSystemView()

    val paths = listOf(
        usrDir,
        view.homeDirectory.toPath()
    )

    logger.info { "Base paths: $paths" }

    var match by remember { mutableStateOf(Match.SOMEWHAT_RELEVANT) }
    var homeState by remember { mutableStateOf(HomeState()) }

    logger.info { "DataLens Initialised!" }

    Window(
        title = "DataLens",
        icon = rememberVectorPainter(FontAwesomeIcons.Solid.Eye),
        onCloseRequest = ::exitApplication,
        resizable = false
    ) {
        DesktopRouter("/") {
            route("/") {
                HomeScreen(
                    state = homeState,
                    onStateChange = { newState ->
                        homeState = newState
                    },
                    // Uses dependency injection and strategy pattern
                    locator = NameLocator(
                        basePaths = paths.filter { it.exists() },
                        repo = IndexRepo(db),
                        algo = Jaccard(),
                        match = match
                    )
                )
            }

            route("/settings") {
                SettingsScreen(
                    match = match,
                    onMatchChange = { newMatch ->
                        match = newMatch
                    }
                )
            }

            noMatch {
                Router.current.navigate(to = "/")
            }
        }


    }
}