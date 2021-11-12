package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
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

        jobViewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.jobs)
            binding.tvEmptyText.isVisible = state.empty
        })

        binding.rvListOfJobs.adapter = adapter
        jobViewModel.dataState.observe(viewLifecycleOwner, { state ->
            binding.pbProgress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            binding.errorGroup.isVisible = state.error
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) { jobViewModel.loadJobs() }
                    .show()
            }
        })

        jobViewModel.networkError.observe(viewLifecycleOwner, {
            Snackbar.make(requireView(), getString(R.string.error_network), Snackbar.LENGTH_LONG)
                .show()
        })

        binding.btRetryButton.setOnClickListener {
            jobViewModel.loadJobs()
        }

        binding.swiperefresh.setOnRefreshListener {
            jobViewModel.refreshJobs()
        }

        binding.rvListOfJobs.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        return binding.root
    }
}