package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.netology.papillon.R
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User
import ru.netology.papillon.model.FeedModel
import ru.netology.papillon.model.FeedModelState
import ru.netology.papillon.repository.PostRepository
import ru.netology.papillon.repository.PostRepositoryImpl
import ru.netology.papillon.utils.SingleLiveEvent

private val empty = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryImpl(AppDbPost.getInstance(context = application).postsDao())

    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)
    val dataPosts = repository.data

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _networkError = SingleLiveEvent<Unit>()
    val networkError: LiveData<Unit>
        get() = _networkError

    val edited = MutableLiveData(empty)

    init {
        loadPosts()
    }

    private fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun savePost() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
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

    fun likedById(id: Long) {
        viewModelScope.launch {
            try {
                repository.likedById(id)
                data.map {
                    FeedModel(posts = data.value?.posts.orEmpty().map { post->
                        if (post.id != id) post else post.copy(
                            likedByMe = !post.likedByMe,
                            likesCnt = post.likesCnt + 1
                        )
                    })
                }
            } catch (e: Exception) {
                _networkError.value = e.message ?: "Network error. Try again later" is
            }
        }
    }

    fun sharedById(id: Long) = repository.sharedById(id)
    fun viewedById(id: Long) = repository.viewedById(id)
    fun removedById(id: Long) = repository.removedById(id)

    fun getPostById(id: Long): LiveData<Post?> = dataPosts.map { posts ->
        posts.find { post ->
            post.id == id
        }
    }
}