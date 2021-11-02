package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.databinding.FragmentAddEditJobBinding
import ru.netology.papillon.dto.Job
import ru.netology.papillon.utils.AndroidUtils
import ru.netology.papillon.utils.JobArg
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.JobViewModel

class AddEditJobFragment : Fragment() {

    companion object {
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

        with(binding) {
            etSetCompany.requestFocus()
            AndroidUtils.showKeyboard(root)

            arguments?.apply {
                textDataCompany?.let { etSetCompany.setText(it) }
                textDataPosition?.let { etSetPosition.setText(it) }
                textDataStart?.let { etSetStart.setText(it) }
                textDataFinish?.let { etSetFinish.setText(it) }
                textDataLink?.let { etSetLink.setText(it) }
            }

            btConfirm.setOnClickListener {
                with(viewModel) {
                    changeData(
                        etSetCompany.text.toString(),
                        etSetPosition.text.toString(),
                        etSetStart.text.toString(),
                        etSetFinish.text.toString(),
                        etSetLink.text.toString(),
                    )
                    saveJob()
                }

                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }

            btCancel.setOnClickListener {
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }

            viewModel.jobCreated.observe(viewLifecycleOwner) {
                viewModel.loadJobs()
                findNavController().navigateUp()
            }

            return root
        }
    }
}