package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import ru.netology.papillon.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun likedById(id: Long)
    suspend fun sharedById(id: Long)
    suspend fun viewedById(id: Long)
    suspend fun removedById(id: Long)
    suspend fun save(post: Post)
}

interface Repository<T> {
    val data: LiveData<List<T>>
    suspend fun getAll()
    suspend fun removedById(id: Long)
    suspend fun save(t: T)
}