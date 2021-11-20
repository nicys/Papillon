package ru.netology.papillon.application

import android.app.Application
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.papillon.auth.AppAuth
import ru.netology.papillon.work.RefreshPostsWorker
import java.util.concurrent.TimeUnit

class PappilonApplication : Application() {
    private val appScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        setupAuth()
        setupWork()
    }

    private fun setupAuth() {
        appScope.launch {
            AppAuth.initApp(this@PappilonApplication)
        }
    }

    private fun setupWork() {
        appScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = PeriodicWorkRequestBuilder<RefreshPostsWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(this@PappilonApplication).enqueueUniquePeriodicWork(
                RefreshPostsWorker.name,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}