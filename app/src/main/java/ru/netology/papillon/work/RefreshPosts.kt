package ru.netology.papillon.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.repository.PostRepository
import ru.netology.papillon.repository.PostRepositoryImpl

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