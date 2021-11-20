package ru.netology.papillon.entity

import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.Attachment
import ru.netology.papillon.dto.Coords
import ru.netology.papillon.dto.Post
import ru.netology.papillon.enumeration.AttachmentType

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
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

) {

    fun toDtoPost() = Post(
        id,
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
                dto.ownedByMe,
                CoordsEmbeddable.fromDto(dto.coords),
                AttachmentEmbeddable.fromDto(dto.attachment),

            )
    }
}

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}

data class CoordsEmbeddable(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
) {
    fun toDto() = Coords(lat, lng)

    companion object {
        fun fromDto(dto: Coords?) = dto?.let {
            CoordsEmbeddable(it.lat, it.lng)
        }
    }
}