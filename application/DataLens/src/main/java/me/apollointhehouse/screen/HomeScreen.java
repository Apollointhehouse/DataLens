package me.apollointhehouse.screen;

import me.apollointhehouse.data.QueryLocator;
import me.apollointhehouse.data.SearchResults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class HomeScreen implements Screen {
    private static final Logger logger = LogManager.getLogger(HomeScreen.class);

    private final Window window;

    private final JTextField search = new JTextField("\uD83D\uDD0D Search", 30);
    private final JButton searchBtn = new JButton("Search");
    private final JTable result = new JTable();

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
//        panel.setLayout(new GridLayout(3, 1));
        panel.setLayout(new GridBagLayout());

        final var searchPanel = addSearch();
        final var resultsPannel = addResults();

        panel.add(searchPanel);
        panel.add(resultsPannel);

        window.add(panel, BorderLayout.CENTER);

        searchBtn.addActionListener(new ActionListener() {
            private Future<?> future = CompletableFuture.allOf();

            @Override
            public void actionPerformed(ActionEvent e) {
                String query = search.getText();

                future.cancel(true);

                var start = System.currentTimeMillis();
                future = executor.submit(() -> {
                    var results = new SearchResults(locator.locate(query).stream().toList());
                    result.setModel(results);
                    var elapsed = System.currentTimeMillis() - start;

                    logger.info("Elapsed: {}", elapsed);
//                    logger.info(paths.toString());
                });
            }
        });
    }

    public JPanel addSearch() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(2, 1));

        searchPanel.add(search);
        searchPanel.add(searchBtn);

        return searchPanel;
    }

    public JPanel addResults() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(1, 1));

        result.setSize(500, 200);
        resultPanel.add(result);

        return resultPanel;
    }
}
