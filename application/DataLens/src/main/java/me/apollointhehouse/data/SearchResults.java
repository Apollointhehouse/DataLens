package me.apollointhehouse.data;

import mslinks.ShellLink;
import mslinks.ShellLinkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SearchResults extends AbstractTableModel {
    private static final Logger logger = LogManager.getLogger(SearchResults.class);
    private final @NotNull List<Path> paths;

    public SearchResults(@NotNull List<Path> paths) {
        this.paths = paths;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return paths.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final var path = paths.get(columnIndex);
        final var name = path.getFileName().toString();

        var typeIndex = name.lastIndexOf('.');
        if (typeIndex < 0) { return name; }

        var type = name.substring(typeIndex, name.length() - 1);
        var shortcutTypes = List.of(".ink");


        if (!shortcutTypes.contains(type)) return name;

        try {
            logger.info("type: {}", type);
            return new ShellLink(path.toFile()).getName();
        } catch (IOException | ShellLinkException e) {
            logger.error(e);
        }

        return name;
    }
}
