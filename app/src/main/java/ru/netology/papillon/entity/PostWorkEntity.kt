package ru.netology.papillon.entity

import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.Post

class PostWorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val author: String,
    @Nullable val authorAvatar: String?,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likesCnt: Int,
    val shares: String,
    val sharesCnt: Int,
    val views: String,
    val viewsCnt: Int,
    @Embedded
    var coords: CoordsEmbeddable?,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    var uri: String? = null,
    val ownedByMe: Boolean
) {

    fun toDtoPost() = Post(
        postId,
        authorId,
        author,
        authorAvatar,
        published,
        content,
        likedByMe,
        likesCnt,
        shares,
        sharesCnt,
        views,
        viewsCnt,
        coords?.toDto(),
        attachment?.toDto(),
        ownedByMe
    )

    companion object {
        fun fromDtoPost(dto: Post) =
            PostEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.published,
                dto.content,
                dto.likedByMe,
                dto.likesCnt,
                dto.shares,
                dto.sharesCnt,
                dto.views,
                dto.viewsCnt,
                CoordsEmbeddable.fromDto(dto.coords),
                AttachmentEmbeddable.fromDto(dto.attachment),
                dto.ownedByMe
            )
    }
}