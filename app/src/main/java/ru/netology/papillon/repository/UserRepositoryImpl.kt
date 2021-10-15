package ru.netology.papillon.repository

import androidx.lifecycle.Transformations
import ru.netology.papillon.dao.UserDao
import ru.netology.papillon.dto.User
import ru.netology.papillon.extensions.toEntityUser

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override fun getAllUsers() = Transformations.map(userDao.getAll()) {
        it.map {
            User(it.idUser, it.avatar, it.entered, it.name, it.surname, it.isMe)
        }
    }

    override fun removedById(idUser: Long) {
        userDao.removedById(idUser)
    }

    override fun saveUser(user: User) {
        userDao.saveUser(user.toEntityUser())
    }
}