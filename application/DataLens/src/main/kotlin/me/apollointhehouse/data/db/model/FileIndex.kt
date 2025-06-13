package me.apollointhehouse.data.db.model

import org.jetbrains.exposed.v1.core.Table

object FileIndex : Table("file_index") {
    val name = varchar("name", 255).index()
    val path = varchar("path", 1024)
    override val primaryKey = PrimaryKey(name, path)
}