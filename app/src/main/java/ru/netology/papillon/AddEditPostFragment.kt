package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.databinding.FragmentAddEditPostBinding
import ru.netology.papillon.utils.AndroidUtils.hideKeyboard
import ru.netology.papillon.utils.AndroidUtils.showKeyboard
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.PostViewModel

class AddEditPostFragment : Fragment() {

    companion object {
        var Bundle.textDataContent: String? by StringArg
        var Bundle.textDataVideo: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddEditPostBinding.inflate(inflater, container, false)

        binding.etEditContent.requestFocus()
        showKeyboard(binding.root)

        arguments?.textDataContent?.let { binding.etEditContent.setText(it) }
        arguments?.textDataVideo?.let { binding.etVideo.setText(it) }

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.etEditContent.text.toString())
            viewModel.changeVideoURL(binding.etVideo.text.toString())
            viewModel.savePost()
            hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }
}