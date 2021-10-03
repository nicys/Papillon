package ru.netology.papillon.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    val id: Long = 0L,
    val company: String = "",
    val position: String = "",
    val start: String = "",
    val finish: String = "",
    val link: String = "",
) : Parcelable
