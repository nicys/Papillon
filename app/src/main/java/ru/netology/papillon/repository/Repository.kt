package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.papillon.dto.Media
import ru.netology.papillon.dto.MediaUpload
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User
import ru.netology.papillon.enumeration.AttachmentType

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
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload, type: AttachmentType)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun authentication(login: String, password: String)
    suspend fun registration(nameUser: String, login: String, password: String)
}

interface Repository<T> {
    val data: Flow<List<T>>
    val dataS: LiveData<List<T>>
    suspend fun getAll()
    suspend fun removedById(id: Long)
    suspend fun save(t: T)
}

interface ProfileRepository {
    val dataPostsFlow: Flow<List<Post>>
    val dataPosts: LiveData<List<Post>>
    suspend fun getUserById(id: Long): User
    suspend fun getAllPosts()
    suspend fun getAllJobs()
    suspend fun likedById(id: Long)
    suspend fun sharedById(id: Long)
    suspend fun viewedById(id: Long)
    suspend fun removedById(id: Long)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload, type: AttachmentType)
    suspend fun upload(upload: MediaUpload): Media
}