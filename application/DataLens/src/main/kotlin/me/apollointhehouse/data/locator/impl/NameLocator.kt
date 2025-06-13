package me.apollointhehouse.data.locator.impl

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import info.debatty.java.stringsimilarity.interfaces.StringDistance
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.apollointhehouse.data.db.model.FileIndex
import me.apollointhehouse.data.db.model.FileIndex.name
import me.apollointhehouse.data.db.model.FileIndex.path
import me.apollointhehouse.data.locator.LocatorError
import me.apollointhehouse.data.locator.LocatorError.IndexingFailed
import me.apollointhehouse.data.io.visitor
import me.apollointhehouse.data.locator.LocatorState
import me.apollointhehouse.data.locator.QueryLocator
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.nio.file.Path
import kotlin.io.path.visitFileTree

class NameLocator(
    private val basePaths: List<Path>,
    private val algo: StringDistance,
) : QueryLocator<String, Result<Set<Path>, LocatorError>> {
    private var _state = MutableStateFlow<LocatorState>(LocatorState.Idle) // Initial state of the locator
    override val state = _state.asStateFlow()

    override suspend fun locate(query: String): Result<Set<Path>, LocatorError> = binding {
        transaction {
            if (!FileIndex.exists()) {
                _state.value = LocatorState.Indexing // Set state to indexing when starting to index files

                indexFiles().bind() // Index files if the index is empty
            }
        }

        _state.value = LocatorState.Locating // Set state to locating when performing a search
        val results = transaction {
            FileIndex
                .selectAll()
                .map { it[path] to algo.distance(it[name].lowercase(), query.lowercase()) } // Calculate distance to query for each file name
                .filter { (_, dist) -> dist < MAX_DIST } // Filter out files that are not similar enough to the query
                .sortedWith(Comparator.comparingDouble { (_, dist) -> dist }) // Sort by distance to query
                .map { (path, _) -> Path.of(path) } // Extract the paths
                .toSet()
        }

        _state.value = LocatorState.Idle
        results
    }

    private fun indexFiles(): Result<Unit, LocatorError> = runCatching {
        logger.info { "Indexing files..." }

        val index: MutableMap<String, Path> = mutableMapOf()
        val hidden: MutableSet<String> = mutableSetOf()
        val visitor = visitor(index, hidden)

        for (base in basePaths) base.visitFileTree(visitor, maxDepth = MAX_DEPTH)

        transaction {
            SchemaUtils.create(FileIndex)

            index.forEach { (name, path) ->
                FileIndex.insertIgnore {
                    it[FileIndex.name] = name
                    it[FileIndex.path] = path.toString()
                }
            }
        }
    }.mapError { IndexingFailed }

    companion object {
        private val logger = KotlinLogging.logger {}

        /**
         * Maximum distance for a match to be considered relevant.
         * This value is based on the StringDistance algorithm used.
         */
        private const val MAX_DIST = 0.92

        /**
         * The maximum depth to search.
         * This is a performance optimization to avoid searching too deep into the file system.
         */
        private const val MAX_DEPTH = 19
    }
}