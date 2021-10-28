package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import ru.netology.papillon.AddEditJobFragment.Companion.textDataCompany
import ru.netology.papillon.AddEditJobFragment.Companion.textDataFinish
import ru.netology.papillon.AddEditJobFragment.Companion.textDataLink
import ru.netology.papillon.AddEditJobFragment.Companion.textDataPosition
import ru.netology.papillon.AddEditJobFragment.Companion.textDataStart
import ru.netology.papillon.adapter.JobsAdapter
import ru.netology.papillon.adapter.OnJobInteractionListener
import ru.netology.papillon.databinding.FragmentJobsBinding
import ru.netology.papillon.dto.Job
import ru.netology.papillon.viewmodel.JobViewModel

class JobsFragment : Fragment() {

    val jobViewModel: JobViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentJobsBinding.inflate(inflater, container, false)

        val adapter = JobsAdapter(object : OnJobInteractionListener {
            override fun onDeleteJob(job: Job) {
                jobViewModel.removedById(job.id)
            }
            override fun onEdinJob(job: Job) {
                jobViewModel.editJob(job)
                findNavController().navigate(R.id.action_jobsFragment_to_addEditJobFragment,
                Bundle().apply {
                    textDataCompany = job.company
                    textDataPosition = job.position
                    textDataStart = job.start
                    textDataFinish = job.finish
                    textDataLink = job.link
                })
            }
        })

        binding.rvListOfJobs.adapter = adapter
        jobViewModel.data.observe(viewLifecycleOwner, { jobs ->
            adapter.submitList(jobs)
        })

        binding.rvListOfJobs.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        return binding.root
    }
}