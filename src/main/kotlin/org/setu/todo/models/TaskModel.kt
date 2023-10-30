package org.setu.todo.models

enum class TaskType {
    Any,
    Work,
    Education,
    Quest,
    Shopping,
    Other,
    Personal
}

data class TaskModel(
    val id: Long, // Unique identifier
    var isDone: Boolean,
    var title: String,
    var type: TaskType, // You can use enums for predefined statuses
    var priority: Int, // 1-5
    val createdAt: String, // Timestamp of task creation
    var dueAt: String // Timestamp of task due date
)