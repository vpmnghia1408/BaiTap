package com.example.btvn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.btvn.service.RetrofitClient

class TaskViewModelFactory(private val apiService: com.example.btvn.service.ApiService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                TaskViewModel(RetrofitClient.apiService)
            }
        }
    }
}
