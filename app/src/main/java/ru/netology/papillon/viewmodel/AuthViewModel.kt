package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.papillon.auth.AppAuth
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.dto.AuthState
import ru.netology.papillon.repository.PostRepository
import ru.netology.papillon.repository.PostRepositoryImpl

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl(AppDbPost.getInstance(context = application).postsDao())

    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

    fun authentication(login: String, password: String) {
        viewModelScope.launch {
            repository.authentication(login, password)
        }
    }

    fun registration(nameUser: String, login: String, password: String) {
        viewModelScope.launch {
            repository.registration(nameUser, login, password)
        }
    }
}