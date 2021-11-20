package ru.netology.papillon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.papillon.dto.Job

@Entity
data class JobWorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val company: String,
    val position: String,
    val start: String,
    val finish: String,
    val link: String,
) {
    fun toDtoJob() = Job(id, company, position, start, finish, link)

    companion object {
        fun fromDtoJob(dto: Job) =
            JobWorkEntity(dto.id, dto.company, dto.position, dto.start, dto.finish, dto.link)
    }
}