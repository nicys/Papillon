package ru.netology.papillon.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.papillon.entity.JobWorkEntity
import ru.netology.papillon.entity.PostWorkEntity
import ru.netology.papillon.entity.UserWorkEntity

@Dao
interface PostWorkDao {
    @Query("SELECT * FROM PostWorkEntity WHERE id = :id")
    suspend fun getPostById(id: Long): PostWorkEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(work: PostWorkEntity): Long

    @Query("DELETE FROM PostWorkEntity WHERE id = :id")
    suspend fun removedById(id: Long)
}

@Dao
interface JobWorkDao {
    @Query("SELECT * FROM JobWorkEntity WHERE id = :id")
    suspend fun getJobById(id: Long): JobWorkEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(work: JobWorkEntity): Long

    @Query("DELETE FROM JobWorkEntity WHERE id = :id")
    suspend fun removedById(id: Long)
}

@Dao
interface UserWorkDao {
    @Query("SELECT * FROM UserWorkEntity WHERE id = :id")
    suspend fun getUserById(id: Long): UserWorkEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(work: UserWorkEntity): Long

    @Query("DELETE FROM UserWorkEntity WHERE id = :id")
    suspend fun removedById(id: Long)
}