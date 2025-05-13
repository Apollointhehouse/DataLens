package me.apollointhehouse.data;

import info.debatty.java.stringsimilarity.interfaces.StringDistance;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class NameLocator implements QueryLocator<@NotNull String, @NotNull Path> {
    private final StringDistance algo;

    public NameLocator(StringDistance algo) {
        this.algo = algo;
    }

    @Override
    public List<Path> locate(@NotNull String query) {
        System.out.println("Locating Query!");

        var path = Path.of("\\\\internal.rotorualakes.school.nz\\Users\\Home\\Students\\21076cameron\\Desktop\\.");

        try (var files = Files.walk(path, 1)) {
            return files
                .filter(p -> {
                    try {
//                        var attributes = Files.readAttributes(path, BasicFileAttributes.class);
                        return !Files.isHidden(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .sorted(Comparator.comparingDouble(p -> algo.distance(p.getFileName().toString(), query)))
                .limit(10)
                .toList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            System.out.println("Access Denied!");
        }

        return List.of();
    }
}
