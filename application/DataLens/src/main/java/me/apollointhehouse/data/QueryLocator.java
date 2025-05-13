package me.apollointhehouse.data;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface QueryLocator<T extends @NotNull Object, R extends @NotNull Object> {
    Collection<R> locate(@NotNull T query);
}
