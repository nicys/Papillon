package ru.netology.papillon.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.papillon.api.Api
import ru.netology.papillon.dao.JobDao
import ru.netology.papillon.dto.Job
import ru.netology.papillon.entity.JobEntity
import ru.netology.papillon.error.ApiError
import ru.netology.papillon.error.NetworkError
import ru.netology.papillon.error.UnknownError
import ru.netology.papillon.extensions.toDtoJob
import ru.netology.papillon.extensions.toEntityJob
import java.io.IOException

class JobRepositoryImpl(private val jobDao: JobDao): Repository<Job> {

    override val data = jobDao.getAll()
        .map(List<JobEntity>::toDtoJob)
        .flowOn(Dispatchers.Default)

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
}