package me.apollointhehouse.data.db.repository.impl

import me.apollointhehouse.data.db.model.FileIndex
import me.apollointhehouse.data.db.repository.IndexRepo
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.nio.file.Path

class ExposedIndexRepo(private val database: Database) : IndexRepo {
    override fun exists(): Boolean = transaction(database) { FileIndex.exists() }

    override fun selectAll(): List<Pair<String, String>> = transaction(database) {
        FileIndex.selectAll().map { it[FileIndex.name] to it[FileIndex.path] }
    }

    override fun createIndex(index: Map<String, Path>) = transaction(database) {
        SchemaUtils.create(FileIndex)
        index.forEach { (name, path) ->
            FileIndex.insertIgnore {
                it[FileIndex.name] = name
                it[FileIndex.path] = path.toString()
            }
        }
    }
}