package me.apollointhehouse.data.io

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.fileVisitor

fun visitor(files: MutableList<Path>) = fileVisitor {
    onPreVisitDirectory { path, _ ->
        // Skip directories that are not readable
        if (Files.isReadable(path)) FileVisitResult.CONTINUE else FileVisitResult.SKIP_SUBTREE
    }

    onVisitFile { path, _ ->
        val name = path.fileName.toString()
        // Exclude Recycle Bin system files
        if (name.startsWith("\$I") || name.startsWith("\$R")) return@onVisitFile FileVisitResult.SKIP_SIBLINGS
        if (!Files.isReadable(path)) return@onVisitFile FileVisitResult.SKIP_SUBTREE
        if (Files.isHidden(path)) return@onVisitFile FileVisitResult.SKIP_SIBLINGS

        // Add the file to the list of files to search
        files.add(path)

        FileVisitResult.CONTINUE
    }

    onVisitFileFailed { path, _ ->
        // Skip files that cannot be visited
        FileVisitResult.SKIP_SUBTREE
    }
}