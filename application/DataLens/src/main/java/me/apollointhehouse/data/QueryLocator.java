package me.apollointhehouse.data;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface QueryLocator<T extends @NotNull Object, R extends @NotNull Object> {
    Stream<R> locate(@NotNull T query);
}
