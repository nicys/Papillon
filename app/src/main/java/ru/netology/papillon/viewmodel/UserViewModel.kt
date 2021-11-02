package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.papillon.db.AppDbUser
import ru.netology.papillon.dto.User
import ru.netology.papillon.model.FeedModelState
import ru.netology.papillon.model.FeedModelUsers
import ru.netology.papillon.repository.Repository
import ru.netology.papillon.repository.UserRepositoryImpl
import ru.netology.papillon.utils.SingleLiveEvent

private val empty = User()

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository<User> =
        UserRepositoryImpl(AppDbUser.getInstance(context = application).usersDao())

    val data: LiveData<FeedModelUsers> = repository.data.map(::FeedModelUsers)
    val dataUsers = repository.data

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

    fun removedById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removedById(id)
                val users = data.value?.users.orEmpty().filter { user -> user.idUser != id }
                data.value?.copy(users = users.orEmpty())
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }
}