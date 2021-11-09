package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback 
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.papillon.databinding.FragmentSignInBinding
import ru.netology.papillon.utils.AndroidUtils.hideKeyboard
import ru.netology.papillon.viewmodel.AuthViewModel


class SignInFragment : Fragment() {

    @ExperimentalCoroutinesApi
    private val authViewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.entrance.setOnClickListener {
            val login: String? = binding.inputLogin.text.toString()
            val password: String? = binding.inputPassword.text.toString()
            if (login == null || password == null) {
                Snackbar.make(binding.root, getString(R.string.dontFilled), Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            } else {
                authViewModel.authentication(login, password)
                hideKeyboard(it)
                findNavController().navigateUp()
            }

        }

        binding.toSingUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}