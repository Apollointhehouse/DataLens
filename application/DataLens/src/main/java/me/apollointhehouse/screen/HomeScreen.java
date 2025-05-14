package me.apollointhehouse.screen;

import me.apollointhehouse.data.QueryLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Path;
import java.util.concurrent.*;

public class HomeScreen implements Screen {
    private static final Logger logger = LogManager.getLogger(HomeScreen.class);

    private final Window window;

    private final TextField search = new TextField("Enter Query!");
    private final QueryLocator<@NotNull String, @NotNull Path> locator;
    private final ExecutorService executor;

    public HomeScreen(@NotNull ExecutorService executor, @NotNull QueryLocator<@NotNull String, @NotNull Path> locator) {
        window = new Window();
        this.executor = executor;
        this.locator = locator;

        init();
    }

    private void init() {
        window.setTitle("Home");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        panel.add(search);

        window.add(panel, BorderLayout.CENTER);

        search.addKeyListener(new KeyListener() {
            private Future<?> future = CompletableFuture.allOf();

            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                String query = search.getText();

                future.cancel(true);

                var start = System.currentTimeMillis();
                future = executor.submit(() -> {
                    var paths = locator.locate(query).stream().map(Path::getFileName).toList();
                    var elapsed = System.currentTimeMillis() - start;

                    logger.info("Elapsed: {}", elapsed);
                    logger.info(paths.toString());
                });
            }
        });
    }
}
