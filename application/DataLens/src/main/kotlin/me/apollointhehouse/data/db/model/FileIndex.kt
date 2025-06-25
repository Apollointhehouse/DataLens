package me.apollointhehouse.data.db.model

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.date

object FileIndex : Table("file_index") {
    val name = varchar("name", 255).index()
    val path = varchar("path", 1024)
    val creationDate = date("creation_date")
    override val primaryKey = PrimaryKey(name, path)
}