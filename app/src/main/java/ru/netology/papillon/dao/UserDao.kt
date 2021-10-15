package ru.netology.papillon.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.papillon.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity ORDER BY idUser")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("DELETE FROM UserEntity WHERE idUser = :idUser")
    fun removedById(idUser: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: UserEntity): Long
}