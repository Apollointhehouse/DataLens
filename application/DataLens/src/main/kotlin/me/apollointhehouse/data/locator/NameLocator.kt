package me.apollointhehouse.data.locator

import info.debatty.java.stringsimilarity.interfaces.StringDistance
import io.github.oshai.kotlinlogging.KotlinLogging
import me.apollointhehouse.data.io.visitor
import java.nio.file.Path
import kotlin.io.path.visitFileTree

private val logger = KotlinLogging.logger {}

class NameLocator(
    private val base: Path,
    private val algo: StringDistance,
    private val index: MutableMap<String, Path> = mutableMapOf()
) : QueryLocator<String, Set<Path>> {
    private val hidden: MutableMap<String, Path> = mutableMapOf()

    private val visitor = visitor(index, hidden)

    override suspend fun locate(query: String): Set<Path> {
        runCatching { base.visitFileTree(visitor, maxDepth = MAX_DEPTH) }
            .onFailure {
                logger.error(it) { "Error while visiting files in $base" }
                return emptySet()
            }

        val res = index
            .asSequence() // Use sequence to avoid creating intermediate lists (performance optimization)
            .map { (name, p) -> p to algo.distance(name.lowercase(), query.lowercase()) }
            .sortedWith(Comparator.comparingDouble { (_, dist) -> dist }) // Sort by distance
            .filter { (_, dist) -> dist < MAX_DIST } // Filter out files that are not similar enough to the query
            .map { (path, _) -> path } // Extract the paths

        return res.toSet()
    }

    companion object {
        /**
         * Maximum distance for a match to be considered relevant.
         * This value is based on the StringDistance algorithm used.
         */
        private const val MAX_DIST = 0.8

        /**
         * The maximum depth to search.
         * This is a performance optimization to avoid searching too deep into the file system.
         */
        private const val MAX_DEPTH = 10
    }
}