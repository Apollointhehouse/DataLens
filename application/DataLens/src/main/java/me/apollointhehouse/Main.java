package me.apollointhehouse;

import me.apollointhehouse.components.Window;

public class Main {
    public static void main(String[] args) {
        System.out.println("DataLens Initialised!");

        var window = new Window.Builder()
            .setTitle("DataLens")
            .build();

        window.draw();
    }
}