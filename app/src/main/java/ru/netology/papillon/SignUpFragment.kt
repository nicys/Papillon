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
import ru.netology.papillon.databinding.FragmentSignUpBinding
import ru.netology.papillon.utils.AndroidUtils.hideKeyboard
import ru.netology.papillon.viewmodel.AuthViewModel


class SignUpFragment : Fragment() {

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
        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.register.setOnClickListener {
            val userName: String? = binding.inputName.text.toString()
            val login: String? = binding.inputLogin2.text.toString()
            val password: String? = binding.inputPassword2.text.toString()
            if (userName == null || login == null || password == null) {
                Snackbar.make(requireView(), getString(R.string.dontFilled), Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            } else {
                authViewModel.registration(userName, login, password)
                hideKeyboard(it)
                findNavController().navigate(R.id.action_signUpFragment_to_jobsFragment)
            }

        }

        binding.toSingIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}