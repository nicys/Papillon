package ru.netology.papillon.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.papillon.auth.AppAuth
import ru.netology.papillon.db.AppDbJob
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.db.AppDbUser
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.MediaUpload
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User
import ru.netology.papillon.enumeration.AttachmentType
import ru.netology.papillon.model.FeedModelJobs
import ru.netology.papillon.model.FeedModelPosts
import ru.netology.papillon.model.FeedModelState
import ru.netology.papillon.model.MediaModel
import ru.netology.papillon.repository.*
import ru.netology.papillon.utils.SingleLiveEvent
import ru.netology.papillon.utils.sumTotalFeed
import java.io.File

private val emptyUser = User()
private val emptyPost = Post()
private val emptyJob = Job()
private val noMedia = MediaModel()

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val appAuth: AppAuth? = null
    private val repository: ProfileRepositoryImpl? = null
    private val postRepository: PostRepository =
        PostRepositoryImpl(AppDbPost.getInstance(context = application).postsDao())

    private val jobRepository: Repository<Job> =
        JobRepositoryImpl(AppDbJob.getInstance(context = application).jobsDao())

    private val userRepository: Repository<User> =
        UserRepositoryImpl(AppDbUser.getInstance(context = application).usersDao())

    @ExperimentalCoroutinesApi
    val dataPosts: LiveData<FeedModelPosts> = AppAuth.getInstance()
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            postRepository.data
                .map { posts ->
                    FeedModelPosts(
                        posts.map { it.copy(ownedByMe = it.authorId == myId) },
                        posts.isEmpty()
                    )
                }
        }.asLiveData(Dispatchers.Default)

    @ExperimentalCoroutinesApi
    val dataJobs: LiveData<FeedModelJobs> = AppAuth.getInstance()
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            jobRepository.data
                .map { jobs ->
                    FeedModelJobs(
                        jobs.map { it.copy(ownedByMe = it.jobId == myId) },
                        jobs.isEmpty()
                    )
                }
        }.asLiveData(Dispatchers.Default)


    val editedPost = MutableLiveData(emptyPost)
    val editedJob = MutableLiveData(emptyJob)
    val myId: Long? = appAuth?.authStateFlow?.value?.id

    private val _dataState = MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _currentUser = MutableLiveData(User())
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _profileUserId = MutableLiveData<Long?>()
    val profileUserId: LiveData<Long?>
        get() = _profileUserId

    private val _media = MutableLiveData(noMedia)
    val media: LiveData<MediaModel>
        get() = _media

    private val _networkError = SingleLiveEvent<String>()
    val networkError: LiveData<String>
        get() = _networkError

    private val _jobCreated = SingleLiveEvent<Unit>()
    val jobCreated: LiveData<Unit>
        get() = _jobCreated

    init {
        loadMyPosts()
//        loadMyJobs()
    }

    fun getUserById() {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                val fetchedUser = _profileUserId.value?.let { repository?.getUserById(it) }
                _currentUser.value = fetchedUser
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun loadMyPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            postRepository.dataPosts.map {
                it.filter {
                    it.authorId == myId }
            }
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun editPost(post: Post) {
        editedPost.value = post
    }

    @ExperimentalCoroutinesApi
    fun likedById(id: Long) {
        viewModelScope.launch {
            try {
                repository?.likedById(id)
                dataPosts.map {
                    FeedModelPosts(posts = dataPosts.value?.posts.orEmpty().map { post ->
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

    @ExperimentalCoroutinesApi
    fun sharedById(id: Long) {
        viewModelScope.launch {
            try {
                repository?.sharedById(id)
                dataPosts.map {
                    FeedModelPosts(posts = dataPosts.value?.posts.orEmpty().map { post ->
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

    @ExperimentalCoroutinesApi
    fun viewedById(id: Long) {
        viewModelScope.launch {
            try {
                repository?.viewedById(id)
                dataPosts.map {
                    FeedModelPosts(posts = dataPosts.value?.posts.orEmpty().map { post ->
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

    @ExperimentalCoroutinesApi
    fun removedByIdPost(id: Long) {
        viewModelScope.launch {
            try {
                repository?.removedById(id)
                val posts = dataPosts.value?.posts.orEmpty().filter { post -> post.id != id }
                dataPosts.value?.copy(posts = posts)
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }

    fun editJob(job: Job) {
        editedJob.value = job
    }

    fun removedByIdJob(id: Long) {
        viewModelScope.launch {
            try {
                repository?.removedById(id)
                val jobs = dataJobs.value?.jobs.orEmpty().filter { job-> job.id != id }
                dataJobs.value?.copy(jobs = jobs.orEmpty())
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }
}