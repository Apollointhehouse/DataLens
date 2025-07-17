package me.apollointhehouse.data.db.repository.impl

import kotlinx.datetime.daysUntil
import kotlinx.datetime.toKotlinLocalDate
import me.apollointhehouse.data.db.model.FileIndex
import me.apollointhehouse.data.db.repository.IndexRepo
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.nio.file.Path
import java.time.LocalDate

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
                it[FileIndex.creationDate] = LocalDate.now().toKotlinLocalDate()
            }
        }
    }

    override fun removeIndex() = transaction(database) {
        SchemaUtils.drop(FileIndex)
    }

    override fun index(): Boolean = transaction(database) {
        // Check if the index table exists and is not empty
//        val creationDate = FileIndex.selectAll().first()[FileIndex.creationDate]
//
//        val daysSince = creationDate.daysUntil(LocalDate.now().toKotlinLocalDate())
//
//        daysSince > 3
//        return !exists()

        true
    }
}