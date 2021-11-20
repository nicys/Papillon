package ru.netology.papillon.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.papillon.entity.JobEntity
import ru.netology.papillon.entity.PostEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM JobEntity ORDER BY id")
    fun getAll(): Flow<List<JobEntity>>

    @Query("SELECT * FROM JobEntity ORDER BY id")
    fun getAllJobs(): LiveData<List<JobEntity>>

    @Query("DELETE FROM JobEntity WHERE id = :id")
    suspend fun removedById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: JobEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(jobs: List<JobEntity>)

    @Query("SELECT * FROM JobEntity WHERE id = :id ")
    suspend fun getJobById(id: Long) : JobEntity
}