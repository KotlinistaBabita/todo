package com.example.todolist.api

import com.example.todolist.dataclass.Task
import retrofit2.Response


import retrofit2.http.GET


interface TaskApiService {
    @GET("todos")
    suspend fun getTasks(): Response<List<Task>>



}

