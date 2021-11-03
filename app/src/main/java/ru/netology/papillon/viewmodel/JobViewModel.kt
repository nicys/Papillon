package ru.netology.papillon.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.papillon.db.AppDbJob
import ru.netology.papillon.dto.Job
import ru.netology.papillon.model.FeedModelJobs
import ru.netology.papillon.model.FeedModelState
import ru.netology.papillon.repository.JobRepositoryImpl
import ru.netology.papillon.repository.Repository
import ru.netology.papillon.utils.SingleLiveEvent

private var empty = Job()

class JobViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository<Job> =
        JobRepositoryImpl(AppDbJob.getInstance(context = application).jobsDao())

    val data: LiveData<FeedModelJobs> = repository.data
        .map(::FeedModelJobs)
        .catch { e -> e.printStackTrace() }
        .asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _jobCreated = SingleLiveEvent<Unit>()
    val jobCreated: LiveData<Unit>
        get() = _jobCreated

    private val _networkError = SingleLiveEvent<String>()
    val networkError: LiveData<String>
        get() = _networkError

    init {
        loadJobs()
    }

    fun loadJobs() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshJobs() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun saveJob() {
        edited.value?.let {
            _jobCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = empty
    }

    fun editJob(job: Job) {
        edited.value = job
    }

    fun changeData(company: String, position: String, start: String, finish: String, link: String) {
        edited.value = edited.value?.copy(
            company = company,
            position = position,
            start = start,
            finish = finish,
            link = link
        )
    }

    fun removedById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removedById(id)
                val jobs = data.value?.jobs.orEmpty().filter { job-> job.id != id }
                data.value?.copy(jobs = jobs.orEmpty())
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }
}