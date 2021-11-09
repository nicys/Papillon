package ru.netology.papillon.application

import android.app.Application
import ru.netology.papillon.auth.AppAuth

class PappilonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppAuth.initApp(this)
    }
}