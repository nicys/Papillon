package ru.netology.papillon.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import ru.netology.papillon.extensions.getCurrentDateTime
import ru.netology.papillon.extensions.toString
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class Post(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorAvatar: String? = null,
    val published: String = "2021-08-17T16:46:58.887547Z",
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
