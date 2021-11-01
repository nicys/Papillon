package ru.netology.papillon.extensions

import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User
import ru.netology.papillon.entity.*
import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun List<PostEntity>.toDtoPost(): List<Post> = map(PostEntity::toDtoPost)
fun List<Post>.toEntityPost(): List<PostEntity> = map(PostEntity::fromDtoPost)

fun List<JobEntity>.toDtoJob(): List<Job> = map(JobEntity::toDtoJob)
fun List<Job>.toEntityJob(): List<JobEntity> = map(JobEntity::fromDtoJob)

fun List<UserEntity>.toDtoUser(): List<User> = map(UserEntity::toDtoUser)
fun List<User>.toEntityUser(): List<UserEntity> = map(UserEntity::fromDtoUser)