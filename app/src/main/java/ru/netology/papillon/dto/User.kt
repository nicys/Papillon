package ru.netology.papillon.dto

data class User(
    val avatar: String? = null,
    val idUser: Long = 0L,
    val login: String = "",
    val name: String = "",
    val surname: String = "",
    val isMe: Boolean = false,
)
