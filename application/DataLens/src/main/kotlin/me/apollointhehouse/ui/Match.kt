package me.apollointhehouse.ui

enum class Match(val dist: Float) {
    EXACT(0.01f),
    VERY_RELEVANT(0.9f),
    RELEVANT(0.5f),
    SOMEWHAT_RELEVANT(0.9f),
    NOT_RELEVANT(1.0f);
}
