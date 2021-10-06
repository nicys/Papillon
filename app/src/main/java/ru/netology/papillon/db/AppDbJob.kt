package ru.netology.papillon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.papillon.dao.JobDao
import ru.netology.papillon.entity.JobEntity

@Database(
    entities = [JobEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDbJob : RoomDatabase() {
    abstract fun jobsDao(): JobDao

    companion object {
        @Volatile
        private var instance: AppDbJob? = null

        fun getInstance(context: Context): AppDbJob {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDbJob::class.java, "appjob.db")
                .build()
    }
}