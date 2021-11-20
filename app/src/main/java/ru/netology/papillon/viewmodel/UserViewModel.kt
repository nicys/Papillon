package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.papillon.db.AppDbUser
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User
import ru.netology.papillon.model.FeedModelState
import ru.netology.papillon.model.FeedModelUsers
import ru.netology.papillon.repository.Repository
import ru.netology.papillon.repository.UserRepositoryImpl
import ru.netology.papillon.utils.SingleLiveEvent
import ru.netology.papillon.work.RemovePostWorker
import ru.netology.papillon.work.RemoveUserWorker

private val empty = User()

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository<User> =
        UserRepositoryImpl(
            AppDbUser.getInstance(context = application).usersDao(),
            AppDbUser.getInstance(context = application).usersWorkDao(),
        )

    private val workManager: WorkManager =
        WorkManager.getInstance(application)

    val data: LiveData<FeedModelUsers> = repository.data
        .map(::FeedModelUsers)
        .catch { e -> e.printStackTrace() }
        .asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _userCreated = SingleLiveEvent<Unit>()
    val userCreated: LiveData<Unit>
        get() = _userCreated

    private val _networkError = SingleLiveEvent<String>()
    val networkError: LiveData<String>
        get() = _networkError

    init {
        loadUsers()
    }

    fun loadUsers() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshUsers() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun saveUser() {
        edited.value?.let {
            _userCreated.value = Unit
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

    fun editUser(user: User) {
        edited.value = user
    }

    fun changeData(name: String) {
        edited.value = edited.value?.copy(
            name = name,
        )
    }

    @ExperimentalCoroutinesApi
    fun removedById(id: Long) {
        val users = data.value?.users.orEmpty()
            .filter { it.idUser != id }
        data.value?.copy(users = users, empty = users.isEmpty())

        viewModelScope.launch {
            try {
                val data = workDataOf(RemovePostWorker.removeKey to id)
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val request = OneTimeWorkRequestBuilder<RemoveUserWorker>()
                    .setInputData(data)
                    .setConstraints(constraints)
                    .build()
                workManager.enqueue(request)

            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun getUserById(id: Long): LiveData<FeedModelUsers> = data.map { FeedModelUsers(users = data.value?.users
        .orEmpty().map {
            if (it.idUser == id) it else empty
        })
    }
}