package me.apollointhehouse.data.db

import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.applicationDir
import org.jetbrains.exposed.v1.jdbc.Database
import java.nio.file.Path
import kotlin.io.path.exists

private val logger = KotlinLogging.logger {}
private val dbDir = Path.of("${if (applicationDir.exists()) "$applicationDir" else "."}/DataLens.db").toAbsolutePath()
val dbConection = Database.connect("jdbc:sqlite:$dbDir", driver = "org.sqlite.JDBC")

fun initDatabase(): Database {
    logger.debug { "Initializing database at $dbDir" }

    return dbConection
}