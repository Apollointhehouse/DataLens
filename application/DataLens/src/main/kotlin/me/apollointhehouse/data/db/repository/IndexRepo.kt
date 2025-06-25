package me.apollointhehouse.data.db.repository

import me.apollointhehouse.data.db.repository.impl.ExposedIndexRepo
import org.jetbrains.exposed.v1.jdbc.Database
import java.nio.file.Path

fun IndexRepo(database: Database): IndexRepo = ExposedIndexRepo(database)

interface IndexRepo {

    /**
     * Checks if the file index exists in the database.
     *
     * @return `true` if the file index exists, `false` otherwise.
     */
    fun exists(): Boolean

    /**
     * Retrieves all file index entries from the database.
     *
     * @return a [Sequence] of [Pair]s, where the first element is the file identifier and the second is the file path as a [String].
     */
    fun selectAll(): List<Pair<String, String>>

    /**
     * Creates or updates the file index in the database.
     *
     * @param index a [Map] where the key is the file identifier and the value is the file path as a [Path].
     */
    fun createIndex(index: Map<String, Path>)

    /**
     * Removes the file index from the database.
     */
    fun removeIndex()

    /**
     * Determines whether the file index should be rebuilt.
     *
     * @return `true` if reindexing is required, `false` otherwise.
     */
    fun shouldReindex(): Boolean
}