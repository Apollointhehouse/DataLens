package me.apollointhehouse.data.locator.impl

import info.debatty.java.stringsimilarity.Jaccard
import kotlinx.coroutines.runBlocking
import me.apollointhehouse.data.Match
import me.apollointhehouse.data.db.repository.IndexRepo
import me.apollointhehouse.data.locator.LocatorState
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
    override fun removeIndex() { index = emptyMap() }
    override fun index(): Boolean = true
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
            repo = FakeIndexRepo(),
            match = Match.NOT_RELEVANT,
            algo = Jaccard()
        )

        when (val state = locator.state.value) {
            is LocatorState.Indexing -> state.job.join() // Wait for indexing to complete
            else -> {} // No indexing in progress
        }

        // Act
        val result = locator.locate(query = fileName)

        // Assert
        assertTrue(result.isOk, message = "Expected locate to succeed, but it failed with: ${result.value}")
        val found = result.value
        assertTrue(found.isNotEmpty(), message = "Expected to find at least one file, but found none.")

        val foundFileNames = found.map { it.fileName.toString() }.also { println(it) }
        assertTrue(foundFileNames.any { it == fileName }, message = "Expected to find file '$fileName' in results, but found: $foundFileNames")
    }
}