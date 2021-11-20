package ru.netology.papillon.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.papillon.db.AppDbJob
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.db.AppDbUser
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.User
import ru.netology.papillon.repository.*

class RefreshPostsWorker(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val name = "ru.netology.papillon.work.RefreshPostsWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val repository: PostRepository =
            PostRepositoryImpl(
                AppDbPost.getInstance(context = applicationContext).postsDao(),
                AppDbPost.getInstance(context = applicationContext).postsWorkDao(),
            )

        try {
            repository.getAll()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}

class RefreshJobsWorker(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val name = "ru.netology.papillon.work.RefreshJobsWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val repository: Repository<Job> =
            JobRepositoryImpl(
                AppDbJob.getInstance(context = applicationContext).jobsDao(),
                AppDbJob.getInstance(context = applicationContext).jobsWorkDao(),
            )

        try {
            repository.getAll()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}

class RefreshUsersWorker(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val name = "ru.netology.papillon.work.RefreshUsersWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val repository: Repository<User> =
            UserRepositoryImpl(
                AppDbUser.getInstance(context = applicationContext).usersDao(),
                AppDbUser.getInstance(context = applicationContext).usersWorkDao(),
            )

        try {
            repository.getAll()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}