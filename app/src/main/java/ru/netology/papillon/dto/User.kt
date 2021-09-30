package ru.netology.papillon.dto

data class User(
    val idUser: Long = 0L,
    val login: String = "",
    val name: String = "",
    val avatar: String? = null,
    val isMe: Boolean = false,
)
