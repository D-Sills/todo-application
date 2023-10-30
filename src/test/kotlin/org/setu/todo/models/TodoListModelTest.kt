package org.setu.todo.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TodoListModelTest {

    private lateinit var todoList: TodoListModel
    private lateinit var task1: TaskModel
    private lateinit var task2: TaskModel

    @BeforeEach
    fun setUp() {
        // Create a sample TodoListModel and tasks for testing
        task1 = TaskModel(1, false, "Task 1", TaskType.Work, 3, "2023-10-29", "2023-11-15")
        task2 = TaskModel(2, true, "Task 2", TaskType.Personal, 2, "2023-10-29", "2023-11-30")
        val tasks: ObservableList<TaskModel> = FXCollections.observableArrayList(task1, task2)
        todoList = TodoListModel(1, "Test List", tasks)
    }

    @Test
    fun testTodoListModelInitialization() {
        assertEquals(1, todoList.id)
        assertEquals("Test List", todoList.name)
        assertEquals(2, todoList.tasks.size)
    }

    @Test
    fun testAddTaskToTodoListModel() {
        val newTask = TaskModel(3, false, "New Task", TaskType.Other, 4, "2023-10-29", "2023-12-15")
        todoList.tasks.add(newTask)
        assertEquals(3, todoList.tasks.size)
        assertTrue(todoList.tasks.contains(newTask))
    }

    @Test
    fun testRemoveTaskFromTodoListModel() {
        todoList.tasks.remove(task1)
        assertEquals(1, todoList.tasks.size)
        assertFalse(todoList.tasks.contains(task1))
    }
}