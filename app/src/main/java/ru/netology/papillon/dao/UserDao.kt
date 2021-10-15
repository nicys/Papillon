package ru.netology.papillon.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.papillon.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM JobEntity ORDER BY id")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("DELETE FROM UserEntity WHERE idUser = :id")
    fun removedById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveJob(user: UserEntity): Long
}