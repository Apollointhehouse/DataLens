package me.apollointhehouse.data.locator.impl

import kotlinx.coroutines.runBlocking
import me.apollointhehouse.data.db.repository.IndexRepo
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertTrue

class FakeIndexRepo : IndexRepo {
    private var index: Map<String, Path> = emptyMap()
    override fun exists() = index.isNotEmpty()
    override fun selectAll() = index.map { it.key to it.value.toString() }
    override fun createIndex(index: Map<String, Path>) { this.index = index }
}

class NameLocatorTest {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `locate should find file by name`() = runBlocking {
        // Arrange
        val fileName = "testfile.txt"
        val filePath = tempDir.resolve(fileName)
        Files.createFile(filePath)

        val locator = NameLocator(
            basePaths = listOf(tempDir),
            repo = FakeIndexRepo()
        )

        // Act
        val result = locator.locate(query = fileName)

        // Assert
        assertTrue(result.isOk)
        val found = result.value
        assertTrue(found.any { it.fileName.toString() == fileName })
    }
}