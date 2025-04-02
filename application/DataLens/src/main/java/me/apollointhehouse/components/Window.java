package me.apollointhehouse.components;

import javax.swing.*;

public class Window {
    private final JFrame frame;

    private Window(String title, int width, int height) {
        this.frame = new JFrame(title);
        frame.setSize(width, height);
    }

    public void draw() {
        frame.setVisible(true);
    }

    public static class Builder {
        private String title = "";
        private int width = 500;
        private int height = 400;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Window build() {
            return new Window(title, width, height);
        }

    }
}
