package ru.netology.papillon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.findNavController
import ru.netology.papillon.auth.AppAuth
import ru.netology.papillon.viewmodel.AuthViewModel

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu?.let {
            it.setGroupVisible(R.id.unauthenticated, !authViewModel.authenticated)
            it.setGroupVisible(R.id.authenticated, authViewModel.authenticated)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signin -> {
                findNavController(R.id.fragment_nav_host).navigate(R.id.action_postsFragment_to_signInFragment)
                AppAuth.getInstance().authStateFlow
                true
            }
            R.id.signup -> {
                findNavController(R.id.fragment_nav_host).navigate(R.id.action_postsFragment_to_signUpFragment)
                AppAuth.getInstance().authStateFlow
                true
            }
            R.id.signout -> {
                findNavController(R.id.fragment_nav_host).navigate(R.id.action_postsFragment_to_exitFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}