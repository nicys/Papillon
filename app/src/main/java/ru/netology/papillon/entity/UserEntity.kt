package ru.netology.papillon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.User

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val idUser: Long,
    val avatar: String?,
    val entered: String,
    val name: String,
    val surname: String,
    val isMe: Boolean,
) {
    fun toDtoUser() = User(idUser, avatar, entered, name, surname, isMe)
}