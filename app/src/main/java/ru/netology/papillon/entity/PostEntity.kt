package ru.netology.papillon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.Post
import ru.netology.papillon.extensions.getCurrentDateTime

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likesCount: Int,
    val shares: String,
    val sharesCnt: Int,
    val views: String,
    val viewsCnt: Int,
    val videoAttach: String?,
    val audioAttach: String?,
    val imageAttach: String?,
) {
    fun toDtoPost() = Post(id, authorId, author, authorAvatar,
        published, content, likedByMe, likesCount, shares, sharesCnt,
        views, viewsCnt, videoAttach, audioAttach, imageAttach)
}