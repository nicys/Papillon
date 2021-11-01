package ru.netology.papillon.model

//Отвечает непосредственно за СОСТОЯНИЕ данных (загружаются, загружены, ошибка)
data class FeedModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
)
