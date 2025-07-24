package me.apollointhehouse.data.locator.impl

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import info.debatty.java.stringsimilarity.interfaces.StringDistance
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.apollointhehouse.data.Match
import me.apollointhehouse.data.db.repository.IndexRepo
import me.apollointhehouse.data.io.visitor
import me.apollointhehouse.data.locator.LocatorError
import me.apollointhehouse.data.locator.LocatorError.IndexingFailed
import me.apollointhehouse.data.locator.LocatorState
import me.apollointhehouse.data.locator.QueryLocator
import java.nio.file.Path
import kotlin.io.path.visitFileTree

class NameLocator(
    private val basePaths: List<Path>, // List of base paths to search for files
    private val algo: StringDistance, // Algorithm used for calculating the distance between file names and the query
    private val repo: IndexRepo, // Repository for managing the file index
    private val match: Match // Maximum distance for a match to be considered relevant.
) : QueryLocator<String, Result<Set<Path>, LocatorError>> {
    private var _state = MutableStateFlow<LocatorState>(LocatorState.Idle) // Initial state of the locator
    override val state = _state.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        val job = scope.launch {
            if (repo.index()) {

                logger.debug { "FileIndex needs to be built, indexing files..." }
                if (repo.exists()) repo.removeIndex()
                indexFiles() // Reindex files if the index needs to be rebuilt
            }

            _state.value = LocatorState.Idle
        }

        _state.value = LocatorState.Indexing(job) // Set state to indexing when reindexing files
    }

    override suspend fun locate(query: String): Result<Set<Path>, LocatorError> = binding {
        if (_state.value is LocatorState.Indexing) {
            return@binding setOf()
        }

        _state.value = LocatorState.Locating // Set state to locating when performing a search

        logger.debug { "Searching for query: $query" }
        val results = repo.selectAll()
            .asSequence()
            .map { (name, path) -> path to algo.distance(name.lowercase(), query.lowercase()) } // Calculate distance to query for each file name
            .filter { (_, dist) -> dist < match.dist } // Filter out files that are not similar enough to the query
            .sortedWith(Comparator.comparingDouble { (_, dist) -> dist }) // Sort by distance to query
            .map { (path, _) -> Path.of(path) } // Extract the paths
            .toSet()

        _state.value = LocatorState.Idle
        results
    }

    private fun indexFiles(): Result<Unit, LocatorError> = binding {
        logger.info { "Indexing files..." }

        val index: MutableMap<String, Path> = mutableMapOf()
        val hidden: MutableSet<String> = mutableSetOf()
        val visitor = visitor(index, hidden)

        runCatching { for (base in basePaths) base.visitFileTree(visitor, maxDepth = MAX_DEPTH) }
            .mapError { IndexingFailed }
            .bind()

        logger.debug { "Creating index!"}
        repo.createIndex(index)

        logger.info { "Finished indexing!" }
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        /**
         * The maximum depth to search.
         * This is a performance optimization to avoid searching too deep into the file system.
         */
        private const val MAX_DEPTH = 19
    }
}