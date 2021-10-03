package ru.netology.papillon.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorAvatar: String? = null,
    val published: String = "",
    val content: String = "",
    val likedByMe: Boolean = false,
    val likesCount: Int = 0,
    val shares: String = "",
    val sharesCnt: Int = 0,
    val views: String = "",
    val viewsCnt: Int = 0,
    val videoAttach: String? = null,
    val audioAttach: String? = null,
    val imageAttach: String? = null,
) : Parcelable
