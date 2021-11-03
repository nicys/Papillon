package ru.netology.papillon.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.papillon.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun likedById(id: Long)
    suspend fun sharedById(id: Long)
    suspend fun viewedById(id: Long)
    suspend fun removedById(id: Long)
    suspend fun save(post: Post)
}

interface Repository<T> {
    val data: Flow<List<T>>
    suspend fun getAll()
    suspend fun removedById(id: Long)
    suspend fun save(t: T)
}