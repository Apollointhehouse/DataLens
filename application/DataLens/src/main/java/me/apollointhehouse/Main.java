package me.apollointhehouse;

import com.formdev.flatlaf.FlatIntelliJLaf;
import me.apollointhehouse.screen.HomeScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("DataLens Initialised!");

        FlatIntelliJLaf.setup();

        SwingUtilities.invokeLater(HomeScreen::new);
    }
}