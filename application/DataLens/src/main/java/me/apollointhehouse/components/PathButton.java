package me.apollointhehouse.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JButton;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;

public class PathButton extends JButton {
    private static final Logger logger = LogManager.getLogger(PathButton.class);

    public PathButton(String text, Path path) {
        super(text);
        setFocusable(false);
        addActionListener(e -> open(path));
    }

    private void open(Path path) {
        if (!Desktop.isDesktopSupported()) { logger.error("Failed to open path!"); }

        try {
            Desktop.getDesktop().open(path.toFile());
        } catch (IOException e) {
            logger.error("Failed to open link!");
        }
    }
}
