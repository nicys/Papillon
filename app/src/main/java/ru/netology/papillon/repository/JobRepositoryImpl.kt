package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.papillon.api.Api
import ru.netology.papillon.dao.JobDao
import ru.netology.papillon.dao.JobWorkDao
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.entity.JobEntity
import ru.netology.papillon.entity.PostEntity
import ru.netology.papillon.error.ApiError
import ru.netology.papillon.error.NetworkError
import ru.netology.papillon.error.UnknownError
import ru.netology.papillon.extensions.toDtoJob
import ru.netology.papillon.extensions.toDtoPost
import ru.netology.papillon.extensions.toEntityJob
import java.io.IOException

class JobRepositoryImpl(
    private val jobDao: JobDao,
    private val jobWorkDao: JobWorkDao,
    ): Repository<Job> {

    override val data = jobDao.getAll()
        .map(List<JobEntity>::toDtoJob)
        .flowOn(Dispatchers.Default)

    override val dataS: LiveData<List<Job>> =
        jobDao.getAllJobs().map(List<JobEntity>::toDtoJob)

    override suspend fun getAll() {
        try {
            val response = Api.service.getAllJobs()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            jobDao.insert(body.toEntityJob())
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }

    override suspend fun save(t: Job) {
        try {
            val response = Api.service.saveJob(t)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            jobDao.insert(JobEntity.fromDtoJob(body))
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }

    override suspend fun removedById(id: Long) {
        try {
            val response = Api.service.removedByIdJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), response.message())
            jobDao.removedById(id)
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }

    override suspend fun processWorkRemoved(id: Long) {
        try {
            val response = Api.service.removedByIdJob(id)
            response.body() ?: throw ApiError(response.code(), response.message())
            jobDao.removedById(id)
            jobWorkDao.removedById(id)
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}