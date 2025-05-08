package me.apollointhehouse.data;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import info.debatty.java.stringsimilarity.*;

public class NameLocator implements QueryLocator<@NotNull String, @NotNull Path> {
    private final LongestCommonSubsequence lcs = new LongestCommonSubsequence();

    @Override
    public Stream<Path> locate(@NotNull String query) {
        System.out.println("Locating Query!");

        var path = Path.of("\\\\internal.rotorualakes.school.nz\\Users\\Home\\Students\\21076cameron\\Desktop\\.");

        try (var files =
                 Files.find(
                     path,
                     1,
                     (p, attr) -> p.toString().compareTo(query) > 0
                 )
        ) {
            return files
                .map(Path::getFileName)
                .limit(10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            System.out.println("Access Denied!");
        }

        return Stream.of();
    }
}
