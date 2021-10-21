package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.ProfileFragment.Companion.userData
import ru.netology.papillon.databinding.FragmentAddEditJobBinding
import ru.netology.papillon.databinding.FragmentAddEditPostBinding
import ru.netology.papillon.dto.Job
import ru.netology.papillon.utils.AndroidUtils
import ru.netology.papillon.utils.JobArg
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.JobViewModel
import ru.netology.papillon.viewmodel.PostViewModel

class AddEditJobFragment : Fragment() {

    companion object {
        var Bundle.dataJob: Job? by JobArg
        var Bundle.textDataCompany: String? by StringArg
        var Bundle.textDataPosition: String? by StringArg
        var Bundle.textDataStart: String? by StringArg
        var Bundle.textDataFinish: String? by StringArg
        var Bundle.textDataLink: String? by StringArg
    }

    private val viewModel: JobViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddEditJobBinding.inflate(inflater, container, false)

        binding.etSetCompany.requestFocus()
        AndroidUtils.showKeyboard(binding.root)

//        arguments?.dataJob?.let { job ->
//            with(binding) {
//                etSetCompany.setText(job.company)
//                etSetPosition.setText(job.position)
//                etSetStart.setText(job.start)
//                etSetFinish.setText(job.finish)
//                etSetLink.setText(job.link)
//
//                btConfirm.setOnClickListener {
//                    viewModel.changeData(
//                        etSetCompany.text.toString(),
//                        etSetPosition.text.toString(),
//                        etSetStart.text.toString(),
//                        etSetFinish.text.toString(),
//                        etSetLink.text.toString(),
//                    )
//                    viewModel.saveJob()
//                    AndroidUtils.hideKeyboard(requireView())
//                    findNavController().navigateUp()
//                }
//            }
//        }


        arguments?.textDataCompany?.let { binding.etSetCompany.setText(it) }
        arguments?.textDataPosition?.let { binding.etSetPosition.setText(it) }
        arguments?.textDataStart?.let { binding.etSetStart.setText(it) }
        arguments?.textDataFinish?.let { binding.etSetFinish.setText(it) }
        arguments?.textDataLink?.let { binding.etSetLink.setText(it) }

        binding.btConfirm.setOnClickListener {
            viewModel.changeCompany(binding.etSetCompany.text.toString())
            viewModel.changePosition(binding.etSetPosition.text.toString())
            viewModel.changeStart(binding.etSetStart.text.toString())
            viewModel.changeFinish(binding.etSetFinish.text.toString())
            viewModel.changeLink(binding.etSetLink.text.toString())
            viewModel.saveJob()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        binding.btCancel.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }
}