package ru.netology.papillon.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import ru.netology.papillon.extensions.getCurrentDateTime

@Parcelize
data class Post(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorAvatar: String? = null,
    val published: String = getCurrentDateTime().toString(),
    val content: String = "",
    val likedByMe: Boolean = false,
    val likesCnt: Int = 0,
    val shares: String = "",
    val sharesCnt: Int = 0,
    val views: String = "",
    val viewsCnt: Int = 0,
    var coords: @RawValue Coords? = null,
    var attachment: @RawValue Attachment? = null,
    val ownedByMe: Boolean = false,
) : Parcelable

data class Attachment(
    val url: String,
    val type: AttachmentType
)

data class Coords(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
)
