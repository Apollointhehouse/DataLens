package me.apollointhehouse.screen;

import javax.swing.*;
import java.awt.*;

public class HomeScreen implements Screen {
    private final Window window;

    public HomeScreen() {
        window = new Window();

        init();
    }

    private void init() {
        window.setTitle("Home");
        window.setTitle("Home");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        panel.add(new TextField("Hello World!"));
        panel.add(new Button("Press Me!"));

        window.add(panel, BorderLayout.CENTER);
    }
}
