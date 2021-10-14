package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.databinding.FragmentAddEditJobBinding
import ru.netology.papillon.databinding.FragmentAddEditPostBinding
import ru.netology.papillon.utils.AndroidUtils
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.JobViewModel
import ru.netology.papillon.viewmodel.PostViewModel

class AddEditJobFragment : Fragment() {

    companion object {
        var Bundle.textDataCompany: String? by StringArg
        var Bundle.textDataPosition: String? by StringArg
        var Bundle.textDataStart: String? by StringArg
        var Bundle.textDataFinish: String? by StringArg
        var Bundle.textDataLink: String? by StringArg
    }

    private val viewModel: JobViewModel by viewModels(
        ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddEditJobBinding.inflate(inflater, container, false)

        binding.etCompany.requestFocus()
        AndroidUtils.showKeyboard(binding.root)

        arguments?.textDataCompany?.let { binding.etCompany.setText(it) }
        arguments?.textDataPosition?.let { binding.etPosition.setText(it) }
        arguments?.textDataStart?.let { binding.etStart.setText(it) }
        arguments?.textDataFinish?.let { binding.etFinish.setText(it) }
        arguments?.textDataLink?.let { binding.etLink.setText(it) }

        binding.btConfirm.setOnClickListener {
            viewModel.changeCompany(binding.etCompany.text.toString())
            viewModel.changePosition(binding.etPosition.text.toString())
            viewModel.changeStart(binding.etStart.text.toString())
            viewModel.changeFinish(binding.etFinish.text.toString())
            viewModel.changeLink(binding.etLink.text.toString())
            viewModel.saveJob()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}