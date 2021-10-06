package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.netology.papillon.adapter.JobsAdapter
import ru.netology.papillon.adapter.OnJobInteractionListener
import ru.netology.papillon.databinding.FragmentJobsBinding
import ru.netology.papillon.dto.Job
import ru.netology.papillon.viewmodel.JobViewModel

class JobsFragment : Fragment() {

    val viewModel: JobViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentJobsBinding.inflate(inflater, container, false)

        val adapter = JobsAdapter(object : OnJobInteractionListener {
            override fun onDeleteJob(job: Job) {}
            override fun onEdinJob(job: Job) {}
        })

        binding.rvListOfJobs.adapter = adapter


        return binding.root
    }
}