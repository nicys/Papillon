package ru.netology.papillon.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.papillon.entity.JobEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM JobEntity ORDER BY id")
    fun getAll(): LiveData<List<JobEntity>>

    @Query("DELETE FROM JobEntity WHERE id = :id")
    fun removedById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveJob(job: JobEntity): Long
}