package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.papillon.dto.Media
import ru.netology.papillon.dto.MediaUpload
import ru.netology.papillon.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    val dataPosts: LiveData<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun likedById(id: Long)
    suspend fun sharedById(id: Long)
    suspend fun viewedById(id: Long)
    suspend fun removedById(id: Long)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
}

interface Repository<T> {
    val data: Flow<List<T>>
    suspend fun getAll()
    suspend fun removedById(id: Long)
    suspend fun save(t: T)
}