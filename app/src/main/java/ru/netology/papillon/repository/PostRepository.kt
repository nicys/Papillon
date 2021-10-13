package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import ru.netology.papillon.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likedById(id: Long)
    fun sharedById(id: Long)
    fun removedById(id: Long)
    fun savePost(post: Post)
}