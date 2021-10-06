package ru.netology.papillon.dto

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import ru.netology.papillon.utils.AndroidUtils
import java.time.Instant
import java.util.*

@Parcelize
data class Post @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorAvatar: String? = null,
    val published: String = AndroidUtils.getCurrentDateTime().toString(),
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