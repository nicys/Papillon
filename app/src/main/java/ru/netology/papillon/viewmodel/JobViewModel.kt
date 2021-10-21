package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.papillon.db.AppDbJob
import ru.netology.papillon.dto.Job
import ru.netology.papillon.repository.JobRepository
import ru.netology.papillon.repository.JobRepositoryImpl

private var empty = Job()

class JobViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JobRepository = JobRepositoryImpl(
        AppDbJob.getInstance(context = application).jobsDao()
    )
    val data = repository.getAllJobs()
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

    fun changeData(company: String, position: String, start: String, finish: String, link: String) {
        empty = empty.copy(
            company = company,
            position = position,
            start = start,
            finish = finish,
            link = link
        )
    }

    fun changeCompany(company: String) {
        empty = empty.copy(
            company = company
        )
    }

    fun changePosition(position: String) {
        empty = empty.copy(
            position = position
        )
    }

    fun changeStart(start: String) {
        empty = empty.copy(
            start = start
        )
    }

    fun changeFinish(finish: String) {
        empty = empty.copy(
            finish = finish
        )
    }

    fun changeLink(link: String) {
        empty = empty.copy(
            link = link
        )
    }

    fun removedById(id: Long) = repository.removedById(id)
}