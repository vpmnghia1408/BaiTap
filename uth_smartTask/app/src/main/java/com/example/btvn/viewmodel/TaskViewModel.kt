package com.example.btvn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btvn.model.Task
import com.example.btvn.service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(private val apiService: ApiService) : ViewModel() {

    data class TaskState(
        val tasks: List<Task> = emptyList(),
        val taskDetail: Task? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state

    init {
        fetchTasks()
    }

    // --- Lấy toàn bộ task ---
    fun fetchTasks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = apiService.getTasks()
                if (response.isSuccess) {
                    // Gán ID fallback nếu bị null hoặc 0
                    val fixedList = response.data.mapIndexed { index, task ->
                        if (task.id == 0) task.copy(id = index + 1) else task
                    }
                    _state.update { it.copy(tasks = fixedList, isLoading = false) }
                } else {
                    _state.update {
                        it.copy(errorMessage = "API Error: ${response.message}", isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = "Network Error: ${e.message}", isLoading = false)
                }
            }
        }
    }

    // --- Lấy chi tiết task ---
    fun fetchTaskDetail(taskId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, taskDetail = null, errorMessage = null) }
            try {
                val response = apiService.getTaskDetail(taskId)
                if (response.isSuccess && response.data != null) {
                    _state.update { it.copy(taskDetail = response.data, isLoading = false) }
                } else {
                    _state.update {
                        it.copy(errorMessage = "Task not found (id=$taskId)", isLoading = false)
                    }
                }
            } catch (e: Exception) {
                val message = if (e.message?.contains("404") == true)
                    "Task not found on server (id=$taskId)"
                else
                    "Network Error: ${e.message}"
                _state.update { it.copy(errorMessage = message, isLoading = false) }
            }
        }
    }

    // --- Xóa task ---
    fun deleteTask(taskId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = apiService.deleteTask(taskId)
                if (response.isSuccess) {
                    fetchTasks()
                    onSuccess()
                } else {
                    _state.update {
                        it.copy(errorMessage = "Delete Error: ${response.message}", isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = "Network Error: ${e.message}", isLoading = false)
                }
            }
        }
    }

    fun clearTaskDetail() {
        _state.update { it.copy(taskDetail = null) }
    }
}
