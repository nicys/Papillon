package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User

interface PostRepository {
    val data: LiveData<List<Post>>
    fun getAll()
    fun likedById(id: Long)
    fun sharedById(id: Long)
    fun viewedById(id: Long)
    fun removedById(id: Long)
    fun save(post: Post)
}

interface Repository<T> {
    val data: LiveData<List<T>>
    fun getAll(): LiveData<List<T>>
    fun removedById(id: Long)
    fun save(t: T)
}

//interface UserRepository {
//    fun getAllUsers(): LiveData<List<User>>
//    fun removedById(id: Long)
//    fun saveUser(user: User)
//}