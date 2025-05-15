package me.apollointhehouse;

import com.formdev.flatlaf.FlatIntelliJLaf;
import info.debatty.java.stringsimilarity.Jaccard;
import me.apollointhehouse.data.NameLocator;
import me.apollointhehouse.screen.HomeScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.SwingUtilities;
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

        // Uses dependency injection pattern in order to allow components of application to be easily swappable and testable
        SwingUtilities.invokeLater(() -> new HomeScreen(executor, new NameLocator(start, new Jaccard())));
    }
}