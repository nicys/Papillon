package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.papillon.db.AppDbJob
import ru.netology.papillon.db.AppDbPost
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.repository.JobRepository
import ru.netology.papillon.repository.JobRepositoryImpl
import ru.netology.papillon.repository.PostRepository
import ru.netology.papillon.repository.PostRepositoryImpl

private val empty = Job()

class JobViewModel(application: Application): AndroidViewModel(application) {

    private val repository: JobRepository = JobRepositoryImpl(
        AppDbJob.getInstance(context = application).jobsDao()
    )
    val data = repository.getAllJob()
    val edited = MutableLiveData(empty)

    fun saveJob() {
        edited.value?.let {
            repository.saveJob(it)
        }
        edited.value = empty
    }

    fun editJob(job: Job) {
        edited.value = job
    }

    fun changeCompany(company: String) {
        val text = company.trim()
        if (edited.value?.company == text) {
            return
        }
        edited.value = edited.value?.copy(company = text)
    }

    fun changePosition(position: String) {
        val text = position.trim()
        if (edited.value?.position == text) {
            return
        }
        edited.value = edited.value?.copy(position = text)
    }

    fun changeStart(start: String) {
        val text = start.trim()
        if (edited.value?.start == text) {
            return
        }
        edited.value = edited.value?.copy(start = text)
    }

    fun changeFinish(finish: String) {
        val text = finish.trim()
        if (edited.value?.finish == text) {
            return
        }
        edited.value = edited.value?.copy(finish = text)
    }

    fun changeLink(link: String) {
        val text = link.trim()
        if (edited.value?.link == text) {
            return
        }
        edited.value = edited.value?.copy(link = text)
    }

    fun removedById(id: Long) = repository.removedById(id)
}