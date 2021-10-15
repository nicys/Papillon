package ru.netology.papillon.repository

import androidx.lifecycle.LiveData
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User

interface PostRepository {
    fun getAllPosts(): LiveData<List<Post>>
    fun likedById(id: Long)
    fun sharedById(id: Long)
    fun removedById(id: Long)
    fun savePost(post: Post)
}

interface JobRepository {
    fun getAllJobs(): LiveData<List<Job>>
    fun removedById(id: Long)
    fun saveJob(job: Job)
}

interface UserRepository {
    fun getAllUsers(): LiveData<List<User>>
    fun removedById(id: Long)
    fun saveUser(user: User)
}