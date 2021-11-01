package ru.netology.papillon.model

import ru.netology.papillon.dto.Post

//Отвечает непосредственно за данные (список постов)
data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)
