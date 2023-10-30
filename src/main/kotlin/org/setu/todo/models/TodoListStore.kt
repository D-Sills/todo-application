package org.setu.todo.models

interface TodoListStore {
    fun findAll(): List<TodoListModel>
    fun findOne(id: Long): TodoListModel?
    fun create(todoList: TodoListModel)
    fun update(todoList: TodoListModel)
    fun delete(todoList: TodoListModel)
    fun deleteAll()
}