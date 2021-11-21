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
    val ownedByMe: Boolean,
    val jobId: Long,
) {
    fun toDtoJob() = Job(id, company, position, start, finish, link, ownedByMe, jobId)

    companion object {
        fun fromDtoJob(dto: Job) =
            JobEntity(dto.id, dto.company, dto.position, dto.start, dto.finish, dto.link, dto.ownedByMe, dto.jobId)
    }
}