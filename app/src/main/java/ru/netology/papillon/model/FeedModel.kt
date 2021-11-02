package ru.netology.papillon.model

import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User

//Отвечают непосредственно за данные (список постов, работу и пользователей)
data class FeedModelPosts(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)

data class FeedModelJobs(
    val jobs: List<Job> = emptyList(),
    val empty: Boolean = false,
)

data class FeedModelUsers(
    val users: List<User> = emptyList(),
    val empty: Boolean = false,
)
