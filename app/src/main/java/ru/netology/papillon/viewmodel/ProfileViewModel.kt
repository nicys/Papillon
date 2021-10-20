package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.papillon.db.AppDbUser
import ru.netology.papillon.dto.User
import ru.netology.papillon.repository.UserRepository
import ru.netology.papillon.repository.UserRepositoryImpl

private val empty = User()

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepositoryImpl(
        AppDbUser.getInstance(context = application).usersDao()
    )

    private val _currentUser = MutableLiveData(User())
    val currentUser: LiveData<User>
        get() = _currentUser

    val data = userRepository.getAllUsers()
    val edited = MutableLiveData(empty)

    fun saveUser() {
        edited.value?.let {
            userRepository.saveUser(it)
        }
        edited.value = empty
    }

    fun editName(user: User) {
        edited.value = user
    }

    fun changeName(name: String) {
        val text = name.trim()
        if (edited.value?.name == text) {
            return
        }
        edited.value = edited.value?.copy(name = text)
    }

    fun removedById(idUser: Long) = userRepository.removedById(idUser)

    fun getUserById(id: Long): LiveData<User?> = data.map { users ->
        users.find {
            it.idUser == id
        }
    }
}