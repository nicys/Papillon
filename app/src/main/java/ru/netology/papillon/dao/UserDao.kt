package ru.netology.papillon.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.papillon.entity.JobEntity
import ru.netology.papillon.entity.PostEntity
import ru.netology.papillon.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity ORDER BY idUser")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM UserEntity ORDER BY idUser")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("DELETE FROM UserEntity WHERE idUser = :idUser")
    suspend fun removedById(idUser: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<UserEntity>)

    @Query("SELECT * FROM UserEntity WHERE idUser = :id ")
    suspend fun getUserById(id: Long) : UserEntity
}