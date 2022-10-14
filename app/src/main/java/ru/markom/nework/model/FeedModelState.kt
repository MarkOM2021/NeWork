package ru.markom.nework.model

data class FeedModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = true,
    val systemError: Boolean = false
)
