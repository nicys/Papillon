package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.papillon.api.Api
import ru.netology.papillon.dao.JobDao
import ru.netology.papillon.dao.PostDao
import ru.netology.papillon.dto.Post
import ru.netology.papillon.entity.PostEntity
import ru.netology.papillon.error.ApiError
import ru.netology.papillon.error.NetworkError
import ru.netology.papillon.error.UnknownError
import ru.netology.papillon.extensions.toDtoPost
import ru.netology.papillon.extensions.toEntityJob
import ru.netology.papillon.extensions.toEntityPost
import java.io.IOException

class ProfileRepositoryImpl(
    private val postDao: PostDao,
    private val jobDao: JobDao,
) : ProfileRepository {

    override val dataPostsFlow = postDao.getAll()
        .map(List<PostEntity>::toDtoPost)
        .flowOn(Dispatchers.Default)

    override val dataPosts: LiveData<List<Post>> =
        postDao.getAllPosts().map(List<PostEntity>::toDtoPost)

    override suspend fun getAllPosts() {
        try {
            val response = Api.service.getAllPosts()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insertPosts(body.toEntityPost())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getAllJobs() {
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
}