package me.apollointhehouse.data

enum class Match(val dist: Float) {
    EXACT(0.01f),
    VERY_RELEVANT(0.80f),
    RELEVANT(0.90f),
    SOMEWHAT_RELEVANT(0.95f),
    NOT_RELEVANT(1.0f);
}