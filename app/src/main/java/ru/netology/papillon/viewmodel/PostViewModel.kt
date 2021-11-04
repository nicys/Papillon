package ru.netology.papillon.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.papillon.model.PhotoModel
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.dto.Post
import ru.netology.papillon.model.FeedModelPosts
import ru.netology.papillon.model.FeedModelState
import ru.netology.papillon.repository.PostRepository
import ru.netology.papillon.repository.PostRepositoryImpl
import ru.netology.papillon.utils.SingleLiveEvent
import ru.netology.papillon.utils.sumTotalFeed
import java.io.File

private val empty = Post()
private val noPhoto = PhotoModel()

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryImpl(AppDbPost.getInstance(context = application).postsDao())

    val data: LiveData<FeedModelPosts> = repository.data
        .map(::FeedModelPosts)
        .catch { e -> e.printStackTrace() }
        .asLiveData(Dispatchers.Default)

    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData()
    }

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _networkError = SingleLiveEvent<String>()
    val networkError: LiveData<String>
        get() = _networkError

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    val edited = MutableLiveData(empty)

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
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

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
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
                    FeedModelPosts(posts = data.value?.posts.orEmpty().map { post ->
                        if (post.id != id) post else post.copy(
                            likedByMe = !post.likedByMe,
                            likesCnt = post.likesCnt + 1
                        )
                    })
                }
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }

    fun sharedById(id: Long) {
        viewModelScope.launch {
            try {
                repository.sharedById(id)
                data.map {
                    FeedModelPosts(posts = data.value?.posts.orEmpty().map { post ->
                        if (post.id != id) post else post.copy(
                            sharesCnt = post.sharesCnt + 1,
                            shares = sumTotalFeed(post.sharesCnt + 1)
                        )
                    })
                }
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }

    fun viewedById(id: Long) {
        viewModelScope.launch {
            try {
                repository.viewedById(id)
                data.map {
                    FeedModelPosts(posts = data.value?.posts.orEmpty().map { post ->
                        if (post.id != id) post else post.copy(
                            viewsCnt = post.viewsCnt + 1,
                            views = sumTotalFeed(post.viewsCnt + 1)
                        )
                    })
                }
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }

    fun removedById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removedById(id)
                val posts = data.value?.posts.orEmpty().filter { post -> post.id != id }
                data.value?.copy(posts = posts.orEmpty())
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }

    fun getPostById(id: Long): LiveData<Post?> = repository.dataPosts.map { posts ->
        posts.find { post ->
            post.id == id
        }
    }
}