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

//class SavePostWorker(
//    applicationContext: Context,
//    params: WorkerParameters
//) : CoroutineWorker(applicationContext, params) {
//    companion object {
//        const val postKey = "post"
//    }
//
//    override suspend fun doWork(): Result {
//        val id = inputData.getLong(postKey, 0L)
//        if (id == 0L) {
//            return Result.failure()
//        }
//        val repository: PostRepository =
//            PostRepositoryImpl(
//                AppDbPost.getInstance(context = applicationContext).postsDao(),
//                AppDbPost.getInstance(context = applicationContext).postsWorkDao(),
//            )
//        return try {
//            repository.processWork(id)
//            Result.success()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//}
//
//class SaveJobWorker(
//    applicationContext: Context,
//    params: WorkerParameters
//) : CoroutineWorker(applicationContext, params) {
//    companion object {
//        const val jobKey = "job"
//    }
//
//    override suspend fun doWork(): Result {
//        val id = inputData.getLong(jobKey, 0L)
//        if (id == 0L) {
//            return Result.failure()
//        }
//        val repository: Repository<Job> =
//            JobRepositoryImpl(
//                AppDbJob.getInstance(context = applicationContext).jobsDao(),
//                AppDbJob.getInstance(context = applicationContext).jobsWorkDao(),
//            )
//        return try {
//            repository.processWork(id)
//            Result.success()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//}
//
//class SaveUserWorker(
//    applicationContext: Context,
//    params: WorkerParameters
//) : CoroutineWorker(applicationContext, params) {
//    companion object {
//        const val userKey = "user"
//    }
//
//    override suspend fun doWork(): Result {
//        val id = inputData.getLong(userKey, 0L)
//        if (id == 0L) {
//            return Result.failure()
//        }
//        val repository: Repository<User> =
//            UserRepositoryImpl(
//                AppDbUser.getInstance(context = applicationContext).usersDao(),
//                AppDbUser.getInstance(context = applicationContext).usersWorkDao(),
//            )
//        return try {
//            repository.processWork(id)
//            Result.success()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//}