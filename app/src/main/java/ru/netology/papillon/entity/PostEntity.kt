package ru.netology.papillon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.Post

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
    val likesCnt: Int,
    val shares: String,
    val sharesCnt: Int,
    val views: String,
    val viewsCnt: Int,
    val videoAttach: String?,
    val audioAttach: String?,
    val imageAttach: String?,
) {
//    fun toDto() = Post(id, authorId, author, authorAvatar, published, content, likedByMe,
//        likesCnt, shares, sharesCnt, views, viewsCnt, videoAttach, audioAttach, imageAttach)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.published, dto.content, dto.likedByMe,
            dto.likesCnt, dto.shares, dto.sharesCnt, dto.views, dto.viewsCnt, dto.videoAttach,
            dto.audioAttach, dto.imageAttach)
    }
}




//{
//    fun toDtoPost() = Post(id, authorId, author, authorAvatar,
//        published, content, likedByMe, likesCnt, shares, sharesCnt,
//        views, viewsCnt, videoAttach, audioAttach, imageAttach)
//}