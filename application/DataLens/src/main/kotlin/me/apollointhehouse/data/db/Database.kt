package me.apollointhehouse.data.db

import org.jetbrains.exposed.v1.jdbc.Database

fun initDatabase() {
    Database.connect("jdbc:sqlite:./DataLens.db", driver = "org.sqlite.JDBC")
}