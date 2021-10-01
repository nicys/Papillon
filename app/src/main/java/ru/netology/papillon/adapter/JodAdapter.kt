package ru.netology.papillon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.papillon.R
import ru.netology.papillon.databinding.CardJobBinding
import ru.netology.papillon.dto.Job

interface OnJobPopupInteractionListener {
    fun onDeleteJob(job: Job)
    fun onEdinJob(job: Job)
}

class JodAdapter(private val onJobPopupInteractionListener: OnJobPopupInteractionListener) :
    ListAdapter<Job, JobViewHolder>(JobDiffItemCallback) {
    object JobDiffItemCallback : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, onJobPopupInteractionListener)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem((position)))
    }
}

class JobViewHolder(
    private val binding: CardJobBinding,
    private val onJobPopupInteractionListener: OnJobPopupInteractionListener
): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(job: Job) {
        with(binding) {
            tvCompany.text = job.company
            tvPosition.text = job.position
            tvPeriod.text = "${ job.start } - ${ job.finish }"
            tvWeb.text = job.link
            ivPopupMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.job_options_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                onJobPopupInteractionListener.onDeleteJob(job)
                                true
                            }
                            R.id.edit -> {
                                onJobPopupInteractionListener.onEdinJob(job)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}