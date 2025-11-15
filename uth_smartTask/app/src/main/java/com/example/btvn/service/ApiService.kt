package com.example.btvn.service

import com.example.btvn.model.DeleteResponse
import com.example.btvn.model.TaskListResponse
import com.example.btvn.model.TaskResponse
import retrofit2.http.*

interface ApiService {

    companion object {
        const val BASE_URL = "https://amock.io/api/researchUTH/"
    }

    @GET("tasks")
    suspend fun getTasks(): TaskListResponse

    @GET("task/{id}")
    suspend fun getTaskDetail(@Path("id") id: Int): TaskResponse

    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): DeleteResponse
}
