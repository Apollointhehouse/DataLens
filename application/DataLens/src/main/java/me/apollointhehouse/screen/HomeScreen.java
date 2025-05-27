package me.apollointhehouse.screen;

import me.apollointhehouse.data.QueryLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.jdesktop.xswingx.PromptSupport;

public class HomeScreen implements Screen {
    private static final Logger logger = LogManager.getLogger(HomeScreen.class);

    private final Window window;

    private final JTextField search = new JTextField("", 30);
    private final JButton searchBtn = new JButton("Search");
//    private final JPanel resultsContainer = new JPanel();

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

        GridBagConstraints gbc = new GridBagConstraints();

        final var searchPanel = addSearch();
        final var resultsPanel = addResults();

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(searchPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(resultsPanel, gbc);

        window.add(panel, BorderLayout.CENTER);

        searchBtn.addActionListener(new ActionListener() {
            private Future<?> future = CompletableFuture.allOf();

            @Override
            public void actionPerformed(ActionEvent e) {
                String query = search.getText();
                search.setText("");

                searchBtn.setText("Locating: " + query);

                future.cancel(true);

                var start = System.currentTimeMillis();
                future = executor.submit(() -> {
                    resultsPanel.removeAll();
                    locator.locate(query).forEach((path) -> {
                        final var result = new JPanel();
                        final var name = new JLabel(path.getFileName().toString());
                        result.add(name);
                        result.setBorder(new TitledBorder(""));

                        resultsPanel.add(result);
                    });
                    var elapsed = System.currentTimeMillis() - start;

                    searchBtn.setText("Search");
                    logger.info("Elapsed: {}", elapsed);
                });
            }
        });
    }

    public JPanel addSearch() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(2, 1));

        PromptSupport.setPrompt("\uD83D\uDD0D Search", search);

        searchPanel.add(search);
        searchPanel.add(searchBtn);

        return searchPanel;
    }

    public JPanel addResults() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

//        resultPanel.setSize(500, 200);

        return resultPanel;
    }
}
