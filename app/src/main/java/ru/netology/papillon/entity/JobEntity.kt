package ru.netology.papillon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.Job

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val company: String,
    val position: String,
    val start: String,
    val finish: String,
    val link: String,
) {
    fun toDtoJob() = Job(id, company, position, start, finish, link)
}