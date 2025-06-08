package me.apollointhehouse.data.locator

import info.debatty.java.stringsimilarity.interfaces.StringDistance
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.io.visitor
import java.nio.file.Path
import java.util.*
import kotlin.io.path.visitFileTree

private val logger = KotlinLogging.logger {}

class NameLocator(private val base: Path, private val algo: StringDistance) : QueryLocator<String, Set<Path>> {
    private val index = mutableMapOf<String, Path>()

    override suspend fun locate(query: String): Set<Path> {
        val visitor = visitor(index)

        try {
            // Visit file tree up to a max depth of 4 in order to avoid performance issues

//            val programs = usr.resolve("/AppData/Roaming/Microsoft/Windows/Start Menu/Programs")

            base.visitFileTree(visitor, maxDepth = MAX_DEPTH)
        } catch (e: Exception) {
            logger.error(e) { "Error while visiting files in $base" }
            return emptySet()
        }

        return index.values
            .asSequence() // Use sequence to avoid creating intermediate lists (performance optimization)
            .sortedWith(Comparator.comparingDouble { p: Path ->
                // Calculate the distance between the file name and the query then sort by it
                val name = p.fileName.toString()
                algo.distance(name.lowercase(), query.lowercase())
            })
            .filter { p ->
                // Filter out files that are not similar enough to the query
                val name = p.fileName.toString()
                algo.distance(name.lowercase(), query.lowercase()) < MAX_DIST
            }
            .toSet()
    }

    companion object {
        /**
         * Maximum distance for a match to be considered relevant.
         * This value is based on the StringDistance algorithm used.
         */
        private const val MAX_DIST = 0.98

        /**
         * The maximum depth to search.
         * This is a performance optimization to avoid searching too deep into the file system.
         */
        private const val MAX_DEPTH = 10
    }
}