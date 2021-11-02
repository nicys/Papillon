package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.databinding.FragmentAddEditUserBinding
import ru.netology.papillon.utils.AndroidUtils
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.UserViewModel

class AddEditUserFragment : Fragment() {

    companion object {
        var Bundle.textUserName: String? by StringArg
    }

    private val userViewModel: UserViewModel by viewModels(
        ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddEditUserBinding.inflate(inflater, container, false)

        with(binding) {
            etUserName.requestFocus()
            AndroidUtils.showKeyboard(binding.root)

            arguments?.textUserName?.let { binding.etUserName.setText(it) }

            btConfirm.setOnClickListener {
                userViewModel.changeData(binding.etUserName.text.toString())
                userViewModel.saveUser()
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }

            btCancel.setOnClickListener {
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }

        userViewModel.userCreated.observe(viewLifecycleOwner) {
            userViewModel.loadUsers()
                findNavController().navigateUp()
            }

            return binding.root
    }
}