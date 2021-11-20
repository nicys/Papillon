package ru.netology.papillon.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.netology.papillon.db.AppDbJob
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.db.AppDbUser
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.User
import ru.netology.papillon.repository.*

class RemovePostWorker(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val removeKey = "ru.netology.papillon.work.RemovePost"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(removeKey, 0L)
        if (id == 0L) {
            return Result.failure()
        }
        val repository: PostRepository =
            PostRepositoryImpl(
                AppDbPost.getInstance(context = applicationContext).postsDao(),
                AppDbPost.getInstance(context = applicationContext).postsWorkDao(),
            )
        return try {
            repository.processWorkRemoved(id)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

class RemoveJobWorker(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val removeKey = "ru.netology.papillon.work.RemoveJob"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(removeKey, 0L)
        if (id == 0L) {
            return Result.failure()
        }
        val repository: Repository<Job> =
            JobRepositoryImpl(
                AppDbJob.getInstance(context = applicationContext).jobsDao(),
                AppDbJob.getInstance(context = applicationContext).jobsWorkDao(),
            )
        return try {
            repository.processWorkRemoved(id)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

class RemoveUserWorker(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val removeKey = "ru.netology.papillon.work.RemoveUser"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(removeKey, 0L)
        if (id == 0L) {
            return Result.failure()
        }
        val repository: Repository<User> =
            UserRepositoryImpl(
                AppDbUser.getInstance(context = applicationContext).usersDao(),
                AppDbUser.getInstance(context = applicationContext).usersWorkDao(),
            )
        return try {
            repository.processWorkRemoved(id)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}