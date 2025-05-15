package me.apollointhehouse.data;

import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.nio.file.Path;
import java.util.List;

public class SearchResults extends AbstractTableModel {
    private final @NotNull List<Path> paths;

    public SearchResults(@NotNull List<Path> paths) {
        this.paths = paths;
    }

    @Override
    public int getRowCount() {
        return paths.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return paths.get(rowIndex).getFileName().toString();
    }
}
