package me.apollointhehouse;

import io.materialtheme.darkstackoverflow.DarkStackOverflowTheme;
import mdlaf.MaterialLookAndFeel;
import me.apollointhehouse.screen.HomeScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("DataLens Initialised!");

        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new DarkStackOverflowTheme()));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(HomeScreen::new);
    }
}