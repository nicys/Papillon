package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.papillon.db.AppDbUser
import ru.netology.papillon.dto.User
import ru.netology.papillon.repository.UserRepository
import ru.netology.papillon.repository.UserRepositoryImpl

private val empty = User()

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository = UserRepositoryImpl(
        AppDbUser.getInstance(context = application).usersDao()
    )
    val data = repository.getAllUsers()
    val edited = MutableLiveData(empty)

    fun saveUser() {
        edited.value?.let {
            repository.saveUser(it)
        }
        edited.value = empty
    }

    fun editUser(user: User) {
        edited.value = user
    }

    fun changeName(name: String) {
        val text = name.trim()
        if (edited.value?.name == text) {
            return
        }
        edited.value = edited.value?.copy(name = text)
    }

    fun removedById(idUser: Long) = repository.removedById(idUser)
}