package ru.netology.papillon.utils

private fun counterOverThousand(feed: Int): Int {
    return when (feed) {
        in 1_000..999_999 -> feed / 100
        else -> feed / 100_000
    }
}

internal fun totalizerSmartFeed(feed: Int): String {
    return when (feed) {
        in 0..999 -> "$feed"
        in 1_000..999_999 -> "${(counterOverThousand(feed).toDouble() / 10)}K"
        else -> "${(counterOverThousand(feed).toDouble() / 10)}M"
    }
}