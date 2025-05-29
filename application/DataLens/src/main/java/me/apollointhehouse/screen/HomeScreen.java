package me.apollointhehouse.screen;

import me.apollointhehouse.components.PathButton;
import me.apollointhehouse.data.QueryLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        final var search = addSearch();
        final var results = addResults();

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(search, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(results, gbc);

        window.add(panel, BorderLayout.CENTER);

        searchBtn.addActionListener(new ActionListener() {
            private Future<?> future = CompletableFuture.allOf();

            @Override
            public void actionPerformed(ActionEvent e) {
                String query = HomeScreen.this.search.getText();
                HomeScreen.this.search.setText("");

                searchBtn.setText("Locating: " + query);

                future.cancel(true);

                var start = System.currentTimeMillis();
                future = executor.submit(() -> {
                    results.removeAll();

                    locator.locate(query).forEach((path) -> addResultPanel(results, path));
                    var elapsed = System.currentTimeMillis() - start;

                    searchBtn.setText("Search");
                    logger.info("Elapsed: {}", elapsed);
                });
            }
        });
    }

    public JPanel addSearch() {
        JPanel search = new JPanel();
        search.setLayout(new GridLayout(2, 1));

        PromptSupport.setPrompt("\uD83D\uDD0D Search", this.search);

        search.add(this.search);
        search.add(searchBtn);

        return search;
    }

    public JPanel addResults() {
        JPanel results = new JPanel();
        results.setLayout(new GridBagLayout());
        results.setSize(100, 100);
        results.setBorder(new TitledBorder("Results"));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(results);
        window.getContentPane().add(scrollPane);
//        scrollPane.setViewportView(results);

//        JPanel resultsContainer = new JPanel();

//        resultsContainer.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

//        resultPanel.add(resultsContainer);
//        resultPanel.add(new JScrollPane(resultsContainer));
//        resultPanel.setSize(500, 200);

        return results;
    }

    private void addResultPanel(JPanel parent, Path path) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = parent.getComponentCount();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.ipady = 5;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.weightx = 1.0;

        JPanel smallPanel = new JPanel();
        smallPanel.setSize(20, 20);
        smallPanel.setBorder(new TitledBorder(""));
        smallPanel.setLayout(new GridLayout(1, 2));
        smallPanel.add(new JLabel(path.getFileName().toString()));

        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new FlowLayout());
        pathPanel.add(new PathButton("Open", path));
        pathPanel.add(new PathButton("Open Location", path.getParent()));

        smallPanel.add(pathPanel, BorderLayout.EAST);

        parent.add(smallPanel, gbc);
        parent.revalidate();
        parent.invalidate();
        parent.repaint();
    }
}
