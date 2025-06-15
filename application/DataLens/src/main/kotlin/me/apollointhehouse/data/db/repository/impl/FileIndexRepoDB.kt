package me.apollointhehouse.data.db.repository.impl

import me.apollointhehouse.data.db.model.FileIndex
import me.apollointhehouse.data.db.repository.FileIndexRepo
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.nio.file.Path

class FileIndexRepoDB(val db: Database) : FileIndexRepo {
    override fun exists(): Boolean = transaction(db) { FileIndex.exists() }

    override fun selectAll(): List<Pair<String, String>> = transaction(db) {
        FileIndex.selectAll().map { it[FileIndex.name] to it[FileIndex.path] }
    }

    override fun createIndex(index: Map<String, Path>) = transaction(db) {
        SchemaUtils.create(FileIndex)
        index.forEach { (name, path) ->
            FileIndex.insertIgnore {
                it[FileIndex.name] = name
                it[FileIndex.path] = path.toString()
            }
        }
    }
}