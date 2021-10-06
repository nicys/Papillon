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

fun User.toEntityUser() = UserEntity(idUser, avatar, login, name, surname, isMe)
fun List<UserEntity>.toDtoUser(): List<User> = map(UserEntity::toDtoUser)
fun List<User>.toEntityUser(): List<UserEntity> = map(User::toEntityUser)

fun Job.toEntityJob() = JobEntity(id, company, position, start, finish, link)
fun List<JobEntity>.toDtoJob() = map(JobEntity::toDtoJob)
fun List<Job>.toJobEntity() = map(Job::toEntityJob)

fun Post.toEntityPost() = PostEntity(id, authorId, author, authorAvatar,
    published, content, likedByMe, likesCount, shares, sharesCnt, views,
    viewsCnt, videoAttach, audioAttach, imageAttach)
fun List<PostEntity>.toDtoPost() = map(PostEntity::toDtoPost)
fun List<Post>.toEntityPost() = map(Post::toEntityPost)