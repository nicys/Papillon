package ru.netology.papillon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.papillon.dao.UserDao
import ru.netology.papillon.dao.UserWorkDao
import ru.netology.papillon.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDbUser : RoomDatabase() {
    abstract fun usersDao(): UserDao
    abstract fun usersWorkDao(): UserWorkDao

    companion object {
        @Volatile
        private var instance: AppDbUser? = null

        fun getInstance(context: Context): AppDbUser {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDbUser::class.java, "appuserb.db")
                .build()
    }
}