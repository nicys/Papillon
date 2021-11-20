package ru.netology.papillon.entity

import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.Post

@Entity
data class PostWorkEntity(
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
    val ownedByMe: Boolean,
    @Embedded
    var coords: CoordsEmbeddable?,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    var uri: String? = null,

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
        ownedByMe,
        coords?.toDto(),
        attachment?.toDto(),

    )

    companion object {
        fun fromDtoPost(dto: Post) =
            PostWorkEntity(
                0L,
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
                dto.ownedByMe,
                CoordsEmbeddable.fromDto(dto.coords),
                AttachmentEmbeddable.fromDto(dto.attachment),
            )
    }
}