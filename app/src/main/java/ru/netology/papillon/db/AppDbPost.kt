package ru.netology.papillon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.papillon.dao.Converters
import ru.netology.papillon.dao.PostDao
import ru.netology.papillon.entity.PostEntity

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDbPost : RoomDatabase() {
    abstract fun postsDao(): PostDao

    companion object {
        @Volatile
        private var instance: AppDbPost? = null

        fun getInstance(context: Context): AppDbPost {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDbPost::class.java, "apppost.db")
                .build()
    }
}