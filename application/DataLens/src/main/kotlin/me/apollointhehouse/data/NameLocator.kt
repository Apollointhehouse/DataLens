package me.apollointhehouse.data

import info.debatty.java.stringsimilarity.interfaces.StringDistance
import io.github.oshai.kotlinlogging.KotlinLogging
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.function.ToDoubleFunction
import kotlin.io.path.fileVisitor
import kotlin.io.path.visitFileTree

private val logger = KotlinLogging.logger {}

class NameLocator(private val usr: Path, private val algo: StringDistance) : QueryLocator<String, Set<Path>> {
    override suspend fun locate(query: String): Set<Path> {
        val files = mutableListOf<Path>()

        val visitor = fileVisitor {
            onPreVisitDirectory { path, _ ->
                // Skip directories that are not readable
                if (Files.isReadable(path)) FileVisitResult.CONTINUE else FileVisitResult.SKIP_SUBTREE
            }

            onVisitFile { path, _ ->
                val name = path.fileName.toString()
                // Exclude Recycle Bin system files
                if (name.startsWith("\$I") || name.startsWith("\$R")) return@onVisitFile FileVisitResult.SKIP_SIBLINGS
                if (!Files.isReadable(path)) return@onVisitFile FileVisitResult.SKIP_SUBTREE
                if (Files.isHidden(path)) return@onVisitFile FileVisitResult.SKIP_SIBLINGS

                // Add the file to the list of files to search
                files.add(path)

                FileVisitResult.CONTINUE
            }

            onVisitFileFailed { path, _ ->
                // Skip files that cannot be visited
                FileVisitResult.SKIP_SUBTREE
            }
        }

        try {
            // Visit file tree up to a max depth of 4 in order to avoid performance issues

            val programs = usr.resolve("/AppData/Roaming/Microsoft/Windows/Start Menu/Programs")

            usr.visitFileTree(visitor, maxDepth = MAX_DEPTH)
            programs.visitFileTree(visitor, maxDepth = MAX_DEPTH)
        } catch (e: Exception) {
            logger.error(e) { "Error while visiting files in $usr" }
            return emptySet()
        }

        return files
            .asSequence() // Use sequence to avoid creating intermediate lists (performance optimization)
            .sortedWith(Comparator.comparingDouble(ToDoubleFunction { p: Path ->
                // Calculate the distance between the file name and the query then sort by it
                val name = p.fileName.toString()
                algo.distance(name.lowercase(Locale.getDefault()), query.lowercase(Locale.getDefault()))
            }))
            .filter { p ->
                // Filter out files that are not similar enough to the query
                val name = p.fileName.toString()
                algo.distance(name.lowercase(Locale.getDefault()), query.lowercase(Locale.getDefault())) < MAX_DIST
            }
            .toSet()
    }

    companion object {
        /**
         * Maximum distance for a match to be considered relevant.
         * This value is based on the StringDistance algorithm used.
         */
        private const val MAX_DIST = 0.92

        /**
         * The maximum depth to search.
         * This is a performance optimization to avoid searching too deep into the file system.
         */
        private const val MAX_DEPTH = 4
    }
}
