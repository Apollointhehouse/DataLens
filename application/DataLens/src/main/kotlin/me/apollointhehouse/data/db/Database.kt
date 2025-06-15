package me.apollointhehouse.data.db

import org.jetbrains.exposed.v1.jdbc.Database

val dbConection = Database.connect("jdbc:sqlite:./DataLens.db", driver = "org.sqlite.JDBC")

fun initDatabase(): Database {
    return dbConection
}