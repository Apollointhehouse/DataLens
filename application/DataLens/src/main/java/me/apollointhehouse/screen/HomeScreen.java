package me.apollointhehouse.screen;

import me.apollointhehouse.data.NameLocator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeScreen implements Screen {
    private final Window window;

    private final TextField search = new TextField("Enter Query!");
    private final NameLocator locator = new NameLocator();

    public HomeScreen() {
        window = new Window();

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
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String query = search.getText();
                var start = System.currentTimeMillis();
                var paths = processWithTimeout(locator.locate(query), 10L, TimeUnit.SECONDS);
                var elapsed = System.currentTimeMillis() - start;

                System.out.println("Elapsed: " + elapsed);
                System.out.println(paths.stream().map(Path::getFileName).toList().toString());
            }
        });
    }

    private static <T> List<T> processWithTimeout(Stream<T> stream, long timeout, TimeUnit unit) {
        CompletableFuture<List<T>> future = CompletableFuture.supplyAsync(() -> stream.collect(Collectors.toList()));

        try {
            return future.get(timeout, unit);
        } catch (Exception e) {
            future.cancel(true); // Attempt to interrupt the stream processing
            // Handle the timeout or interruption appropriately
            return List.of(); // Return an empty list or throw a custom exception
        }
    }

}
