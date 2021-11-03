package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.netology.papillon.api.Api
import ru.netology.papillon.dao.PostDao
import ru.netology.papillon.dto.Post
import ru.netology.papillon.entity.PostEntity
import ru.netology.papillon.error.ApiError
import ru.netology.papillon.error.AppError
import ru.netology.papillon.error.NetworkError
import ru.netology.papillon.error.UnknownError
import ru.netology.papillon.extensions.toDtoPost
import ru.netology.papillon.extensions.toEntityPost
import java.io.IOException

class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {
    override val data = postDao.getAll()
        .map(List<PostEntity>::toDtoPost)
        .flowOn(Dispatchers.Default)

    override val dataPosts: LiveData<List<Post>> =
        postDao.getAllPosts().map(List<PostEntity>::toDtoPost)

    override suspend fun getAll() {
        try {
            val response = Api.service.getAllPosts()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(body.toEntityPost())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            try {
                delay(10_000L)
                val response = Api.service.getNewerPost(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                postDao.insert(body.toEntityPost())
                emit(body.size)
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw UnknownError
            }
        }
    }
        .flowOn(Dispatchers.Default)

    override suspend fun save(post: Post) {
        try {
            val response = Api.service.savePost(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDtoPost(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likedById(id: Long) {
        try {
            val response = Api.service.likedByIdPost(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.likedById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun sharedById(id: Long) {
        try {
            val response = Api.service.sharedByIdPost(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.sharedById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }

    override suspend fun viewedById(id: Long) {
        try {
            val response = Api.service.viewedByIdPost(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.viewedById(id)
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }

    override suspend fun removedById(id: Long) {
        try {
            val response = Api.service.removedByIdPost(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.removedById(id)
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }
}