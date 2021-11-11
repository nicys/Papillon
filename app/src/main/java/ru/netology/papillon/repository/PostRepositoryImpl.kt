package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.papillon.api.Api
import ru.netology.papillon.auth.AppAuth
import ru.netology.papillon.dao.PostDao
import ru.netology.papillon.dto.*
import ru.netology.papillon.entity.PostEntity
import ru.netology.papillon.enumeration.AttachmentType
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
            postDao.insertPosts(body.toEntityPost())
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
                postDao.insertPosts(body.toEntityPost())
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
            postDao.insertPost(PostEntity.fromDtoPost(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            // TODO: add support for other types
            val postWithAttachment = post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )

            val response = Api.service.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())
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

//    override suspend fun removedById(id: Long) {
//            postDao.removedById(id)
//            try {
//                val response = Api.service.removedByIdPost(id)
//                if (!response.isSuccessful) {
//                    throw ApiError(response.code(), response.message())
//                }
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
//    }

    override suspend fun removedById(id: Long) {
        try {
            val response = Api.service.removedByIdPost(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.removedById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun authentication(login: String, password: String) {
        try {
            val response = Api.service.updateUser(login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { AppAuth.getInstance().setAuth(authState.id, it) }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun registration(nameUser: String, login: String, password: String) {
        try {
            val response = Api.service.registrationUser(nameUser, login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { AppAuth.getInstance().setAuth(authState.id, it) }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}