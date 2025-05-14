package me.apollointhehouse;

import com.formdev.flatlaf.FlatIntelliJLaf;
import me.apollointhehouse.screen.HomeScreen;
import javax.swing.*;
import me.apollointhehouse.data.NameLocator;
import info.debatty.java.stringsimilarity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Path start = Path.of("\\\\internal.rotorualakes.school.nz\\Users\\Home\\Students\\21076cameron\\Desktop\\.");

    public static void main(String[] args) {
        logger.info("DataLens Initialised!");

        FlatIntelliJLaf.setup();

        SwingUtilities.invokeLater(() -> new HomeScreen(executor, new NameLocator(start, new Jaccard())));
    }
}