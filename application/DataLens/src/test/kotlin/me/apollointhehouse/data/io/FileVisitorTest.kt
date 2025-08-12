package me.apollointhehouse.data.io

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class FileVisitorTest {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `visitor indexes files and skips hidden and unvisitable files`() {
        val visibleFile = Files.createFile(tempDir.resolve("visible.txt"))
        val hiddenFile = Files.createFile(tempDir.resolve(".hidden.txt"))
        val noVisit = mutableSetOf<String>()
        val index = mutableMapOf<String, Path>()

        // Mark hidden file as hidden (platform dependent)
        try {
            Files.setAttribute(hiddenFile, "dos:hidden", true)
        } catch (_: Exception) {
            // Ignore if not supported
        }

        Files.walkFileTree(tempDir, visitor(index, noVisit))

        assertTrue(index.containsKey("visible.txt"))
        assertFalse(index.containsKey(".hidden.txt"))
        assertTrue(noVisit.contains(".hidden.txt") || noVisit.isEmpty())
    }
}