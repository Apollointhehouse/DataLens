package me.apollointhehouse;

import com.formdev.flatlaf.FlatIntelliJLaf;
import me.apollointhehouse.screen.HomeScreen;
import javax.swing.*;
import me.apollointhehouse.data.NameLocator;
import info.debatty.java.stringsimilarity.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("DataLens Initialised!");

        FlatIntelliJLaf.setup();

        SwingUtilities.invokeLater(() -> new HomeScreen(executor, new NameLocator(new Jaccard())));
    }
}