package ru.netology.papillon.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val idUser: Long = 0L,
    val avatar: String? = null,
    val entered: String = "2021-08-17T16:46:58.887547Z",
    val name: String = "",
    val login: String = "",
    val isMe: Boolean = false,
    val userId: Long = 0L,
) : Parcelable
