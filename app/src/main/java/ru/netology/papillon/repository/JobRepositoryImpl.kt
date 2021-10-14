package ru.netology.papillon.repository

import androidx.lifecycle.Transformations
import ru.netology.papillon.dao.JobDao
import ru.netology.papillon.dto.Job
import ru.netology.papillon.extensions.toEntityJob

class JobRepositoryImpl(
    private val jobDao: JobDao
) : JobRepository {
    override fun getAllJob() = Transformations.map(jobDao.getAll()) {
        it.map {
            Job(it.id, it.company, it.position, it.start, it.finish, it.link)
        }
    }

    override fun removedById(id: Long) {
        jobDao.removedById(id)
    }

    override fun saveJob(job: Job) {
        jobDao.saveJob(job.toEntityJob())
    }
}