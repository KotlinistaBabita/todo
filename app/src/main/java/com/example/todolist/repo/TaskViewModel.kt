package com.example.todolist.repo

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.dataclass.Task
import com.example.todolist.dp.TaskDatabase
import com.google.firebase.crashlytics.FirebaseCrashlytics

import kotlinx.coroutines.launch


class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val allTasks: LiveData<List<Task>>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.getAllTasks()

        viewModelScope.launch {
            repository.fetchTasksFromApi()
        }
    }

    fun insert(task: Task) = viewModelScope.launch { repository.insert(task) }
    fun update(task: Task) = viewModelScope.launch { repository.update(task) }
    fun delete(task: Task) = viewModelScope.launch { repository.delete(task) }

    fun triggerDatabaseErr() {
        try {
            val task = null
            println(task!!.toString())
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

}