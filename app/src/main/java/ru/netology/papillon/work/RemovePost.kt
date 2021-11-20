package ru.netology.papillon.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.repository.PostRepository
import ru.netology.papillon.repository.PostRepositoryImpl

class RemovePostWorker(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val removeKey = "ru.netology.work.RemovePost"
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