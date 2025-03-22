package com.example.todolist.repo

import androidx.lifecycle.LiveData

import com.example.todolist.api.RetrofitInstance
import com.example.todolist.dataclass.Task
import com.example.todolist.dp.TaskDao


class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }


    suspend fun fetchTasksFromApi() {
        try {
            val localTasks = taskDao.getAllTasksList()
            if (localTasks.isEmpty()) {
                val response = RetrofitInstance.api.getTasks()
                if (response.isSuccessful) {
                    response.body()?.let { apiTasks ->
                        taskDao.insertAll(apiTasks)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}