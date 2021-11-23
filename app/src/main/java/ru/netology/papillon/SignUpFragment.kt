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
import ru.netology.papillon.AddEditJobFragment.Companion.textDataCompany
import ru.netology.papillon.AddEditJobFragment.Companion.textDataFinish
import ru.netology.papillon.AddEditJobFragment.Companion.textDataLink
import ru.netology.papillon.AddEditJobFragment.Companion.textDataPosition
import ru.netology.papillon.AddEditJobFragment.Companion.textDataStart
import ru.netology.papillon.databinding.FragmentSignUpBinding
import ru.netology.papillon.utils.AndroidUtils
import ru.netology.papillon.utils.AndroidUtils.hideKeyboard
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.AuthViewModel
import ru.netology.papillon.viewmodel.UserViewModel


class SignUpFragment : Fragment() {

    companion object {
        var Bundle.textUserName: String? by StringArg
        var Bundle.textLoginName: String? by StringArg
    }

    @ExperimentalCoroutinesApi
    private val authViewModel: AuthViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val userViewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)


    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        with(binding) {
            inputName.requestFocus()
            AndroidUtils.showKeyboard(root)
        }

        binding.register.setOnClickListener {

            arguments?.apply {
                textUserName?.let { binding.inputName.setText(it) }
                textLoginName?.let { binding.inputLogin2.setText(it) }
            }

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

                userViewModel.changeData(userName)
                userViewModel.saveUser()

                findNavController().navigate(R.id.action_signUpFragment_to_profileFragment)
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