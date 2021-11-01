package ru.netology.papillon.repository

import androidx.lifecycle.map
import ru.netology.papillon.api.Api
import ru.netology.papillon.dao.UserDao
import ru.netology.papillon.dto.User
import ru.netology.papillon.entity.UserEntity
import ru.netology.papillon.error.ApiError
import ru.netology.papillon.error.NetworkError
import ru.netology.papillon.error.UnknownError
import ru.netology.papillon.extensions.toDtoUser
import ru.netology.papillon.extensions.toEntityUser
import java.io.IOException

class UserRepositoryImpl(private val userDao: UserDao) : Repository<User> {

    override val data = userDao.getAll().map(List<UserEntity>::toDtoUser)

    override suspend fun getAll() {
        try {
            val response = Api.service.getAllUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            userDao.insert(body.toEntityUser())
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }

    override suspend fun save(t: User) {
        try {
            val response = Api.service.saveUser(t)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            userDao.insert(UserEntity.fromDtoUser(body))
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }

    override suspend fun removedById(id: Long) {
        try {
            val response = Api.service.removedByIdUser(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), response.message())
            userDao.removedById(id)
        } catch (e: IOException) {
            NetworkError
        } catch (e: Exception) {
            UnknownError
        }
    }
}