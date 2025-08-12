package me.apollointhehouse.data.locator.impl

import me.apollointhehouse.data.db.model.FileIndex
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.test.assertTrue

class DatabaseTest {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `database connection should be established`() {
        // Check if the database file exists
        val dbFile = Path.of("$tempDir/DataLens.db")
        if (!dbFile.exists()) {
            dbFile.toFile().createNewFile()
        }

        // Connect to the database
        val dbConnection = Database.connect("jdbc:sqlite:$dbFile", driver = "org.sqlite.JDBC")


        transaction(dbConnection) {
            // Create the FileIndex table if it does not exist
            if (!FileIndex.exists()) SchemaUtils.create(FileIndex)

            assertTrue(FileIndex.exists(), message = "FileIndex table should exist after creation")
        }
    }
}