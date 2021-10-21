package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User
import ru.netology.papillon.repository.PostRepository
import ru.netology.papillon.repository.PostRepositoryImpl

private val empty = Post()

class PostViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(
        AppDbPost.getInstance(context = application).postsDao()
    )
    val data = repository.getAllPosts()
    val edited = MutableLiveData(empty)

    fun savePost() {
        edited.value?.let {
            repository.savePost(it)
        }
        edited.value = empty
    }

    fun editPost(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun changeVideoURL(videoURL: String) {
        val text = videoURL
        if (edited.value?.videoAttach == text) {
            return
        }
        edited.value = edited.value?.copy(videoAttach = text)
    }

    fun likedById(id: Long) = repository.likedById(id)
    fun sharedById(id: Long) = repository.sharedById(id)
    fun viewedById(id: Long) = repository.viewedById(id)
    fun removedById(id: Long) = repository.removedById(id)

    fun getPostById(id: Long): LiveData<Post?> = data.map { posts ->
        posts.find { post->
            post.id == id
        }
    }
 }