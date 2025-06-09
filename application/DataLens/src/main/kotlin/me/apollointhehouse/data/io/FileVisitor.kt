package me.apollointhehouse.data.io

import io.github.oshai.kotlinlogging.KotlinLogging
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.fileVisitor

private val logger = KotlinLogging.logger {}

fun visitor(index: MutableMap<String, Path>, hidden: MutableMap<String, Path>) = fileVisitor {
    onPreVisitDirectory { path, _ ->
        val name = path.fileName?.toString() ?: run {
//            logger.warn { "File with no name found at $path, skipping." }
            return@onPreVisitDirectory FileVisitResult.CONTINUE
        }

        if (name in index) {
            // If the directory is already indexed, skip it
//            logger.info { "Directory $name is already indexed, skipping." }
            return@onPreVisitDirectory FileVisitResult.SKIP_SUBTREE
        }

        if (name in hidden) {
            // If the directory is hidden, skip it
//            logger.info { "Skipping hidden directory: $path" }
            return@onPreVisitDirectory FileVisitResult.SKIP_SUBTREE
        }
        if (Files.isHidden(path)) {
//            logger.info { "Indexing hidden directory: $path" }
            hidden[name] = path
            return@onPreVisitDirectory FileVisitResult.SKIP_SUBTREE
        }

        FileVisitResult.CONTINUE
    }

    onVisitFile { path, _ ->
        val name = path.fileName?.toString() ?: run {
//            logger.warn { "File with no name found at $path, skipping." }
            return@onVisitFile FileVisitResult.CONTINUE
        }

        if (name in index) {
            // If the file is already indexed, skip it
//            logger.info { "File $name is already indexed, skipping." }
            return@onVisitFile FileVisitResult.CONTINUE
        }

        // Exclude Recycle Bin system files
        if (name.startsWith("\$I") || name.startsWith("\$R")) return@onVisitFile FileVisitResult.SKIP_SIBLINGS
        if (name.lowercase().startsWith("ntuser")) return@onVisitFile FileVisitResult.CONTINUE

        // Skip hidden files
        if (Files.isHidden(path)) {
//            logger.info { "Skipping hidden file: $path" }
            return@onVisitFile FileVisitResult.CONTINUE
        }

        // Add the file to the list of files to search
//        logger.info { "Adding $path to index" }
        index[name] = path

        FileVisitResult.CONTINUE
    }

    onVisitFileFailed { path, _ ->
        // Skip files that cannot be visited
//        logger.error { "File $path could not be visited" }

        FileVisitResult.CONTINUE
    }
}