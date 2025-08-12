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

    /**
     * This test checks that the locator can find a file by its name.
     * It creates a temporary file, indexes it, and then searches for it.
     * The test ensures that the file is found and that the results are as expected.
     */
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

    /**
     * This test checks that the locator can handle a case where the file does not exist.
     * It should return an empty set without throwing an error.
     * This is a boundary case to ensure robustness of the locator.
     */
    @Test
    fun `locate should return empty set for non-existing file`() = runBlocking {
        // Arrange
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
        val result = locator.locate(query = "nonexistentfile.txt")

        // Assert
        assertTrue(result.isOk, message = "Expected locate to succeed, but it failed with: ${result.value}")
        assertTrue(result.value.isEmpty(), message = "Expected to find no files, but found: ${result.value}")
    }

    /**
     * This test checks that the locator can handle an empty query gracefully.
     * It should return an empty set without throwing an error.
     * This is a boundary case to ensure robustness of the locator.
     */
    @Test
    fun `locate should handle empty query gracefully`() = runBlocking {
        // Arrange
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
        val result = locator.locate(query = "")

        // Assert
        assertTrue(result.isOk, message = "Expected locate to succeed, but it failed with: ${result.value}")
        assertTrue(result.value.isEmpty(), message = "Expected to find no files for empty query, but found: ${result.value}")
    }
}