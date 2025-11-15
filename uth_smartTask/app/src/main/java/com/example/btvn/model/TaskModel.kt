package com.example.btvn.model

data class TaskResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: Task
)

data class TaskListResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<Task> // Kiểu trả về cho GET all tasks
)

data class Task(
    val id: Int,
    val title: String,
    val description: String?,
    val status: String,
    val priority: String,
    val category: String,
    val dueDate: String,
    val desImageURL: String?, // Thêm trường image URL
    val createdAt: String?,
    val updatedAt: String?,
    val subtasks: List<Subtask>? = null,
    val attachments: List<Attachment>? = null,
    val reminders: List<Reminder>? = null
)

data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

data class Attachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String
)

data class Reminder(
    val id: Int,
    val time: String,
    val type: String
)

data class DeleteResponse(
    val isSuccess: Boolean,
    val message: String
)
