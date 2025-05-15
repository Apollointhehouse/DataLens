package me.apollointhehouse.data;

import info.debatty.java.stringsimilarity.interfaces.StringDistance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class NameLocator implements QueryLocator<@NotNull String, @NotNull Path> {
    private static final Logger logger = LogManager.getLogger(NameLocator.class);

    private final Path start;
    private final StringDistance algo;

    public NameLocator(Path start, StringDistance algo) {
        this.start = start;
        this.algo = algo;
    }

    @Override
    public List<Path> locate(@NotNull String query) {
        logger.info("Locating Query!");

        try (var files = Files.walk(start, 2)) {
            return files
                .filter(p -> {
                    try {
                        return !Files.isHidden(p);
                    } catch (IOException e) {
                        logger.error("File hidden check failed! ", e);
                    }
                    return false;
                })
                .sorted(Comparator.comparingDouble(p -> algo.distance(p.getFileName().toString().toLowerCase(), query.toLowerCase())))
                .limit(10)
                .toList();
        } catch (IOException e) {
            logger.error("Failed! ", e);
        } catch (SecurityException e) {
            logger.error("Access Denied!", e);
        }

        return List.of();
    }
}
