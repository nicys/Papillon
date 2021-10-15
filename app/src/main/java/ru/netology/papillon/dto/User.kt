package ru.netology.papillon.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val idUser: Long = 0L,
    val avatar: String? = null,
    val entered: String = "",
    val name: String = "",
    val surname: String = "",
    val isMe: Boolean = false,
) : Parcelable
