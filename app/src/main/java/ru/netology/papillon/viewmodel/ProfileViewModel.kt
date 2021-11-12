package ru.netology.papillon.viewmodel

import android.app.Application
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
import ru.netology.papillon.dto.User
import ru.netology.papillon.model.FeedModelPosts
import ru.netology.papillon.model.FeedModelState
import ru.netology.papillon.repository.*

private val empty = User()

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
    val data: LiveData<FeedModelPosts> = AppAuth.getInstance()
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



    val myId: Long? = appAuth?.authStateFlow?.value?.id

    private val _dataState = MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _currentUser = MutableLiveData(User())
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _profileUserId = MutableLiveData<Long?>()
    val profileUserId: LiveData<Long?>
        get() = _profileUserId

    init {
        loadMyPosts()
//        loadMyJobs()
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

//    fun loadMyJobs() = viewModelScope.launch {
//        try {
//            _dataState.value = FeedModelState(loading = true)
//            jobRepository.dataS.map {
//                it.filter {
//                    it.authorId == myId }
//            }
//            _dataState.value = FeedModelState()
//        } catch (e: Exception) {
//            _dataState.value = FeedModelState(error = true)
//        }
//    }

















//    val dataUser = userRepository.data

    //    private val _currentUser = MutableLiveData(User())
//    val currentUser: LiveData<User>
//        get() = _currentUser
//
//    val data = userRepository.getAllUsers()
//
//    val edited = MutableLiveData(empty)
//
//    fun saveUser() {
//        edited.value?.let {
//            userRepository.saveUser(it)
//        }
//        edited.value = empty
//    }
//
//    fun editName(user: User) {
//        edited.value = user
//    }
//
//    fun changeName(name: String) {
//        val text = name.trim()
//        if (edited.value?.name == text) {
//            return
//        }
//        edited.value = edited.value?.copy(name = text)
//    }
//
//    fun removedById(idUser: Long) = userRepository.removedById(idUser)
//
}