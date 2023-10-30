package org.setu.todo.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskModelTest {

    private lateinit var task: TaskModel

    @BeforeEach
    fun setUp() {
        // Create a sample TaskModel for testing
        task = TaskModel(1, false, "Test Task", TaskType.Work, 3, "2023-10-29", "2023-11-15")
    }

    @Test
    fun testTaskModelInitialization() {
        assertEquals(1, task.id)
        assertFalse(task.isDone)
        assertEquals("Test Task", task.title)
        assertEquals(TaskType.Work, task.type)
        assertEquals(3, task.priority)
        assertEquals("2023-10-29", task.createdAt)
        assertEquals("2023-11-15", task.dueAt)
    }

    @Test
    fun testTaskModelUpdate() {
        task.title = "Updated Task"
        task.isDone = true
        task.type = TaskType.Personal
        task.priority = 4
        task.dueAt = "2023-12-31"

        assertEquals("Updated Task", task.title)
        assertTrue(task.isDone)
        assertEquals(TaskType.Personal, task.type)
        assertEquals(4, task.priority)
        assertEquals("2023-12-31", task.dueAt)
    }

    @Test
    fun testTaskModelEquality() {
        val task2 = TaskModel(1, false, "Test Task", TaskType.Work, 3, "2023-10-29", "2023-11-15")
        val task3 = TaskModel(2, true, "Different Task", TaskType.Personal, 4, "2023-11-01", "2023-12-31")

        assertEquals(task, task2)
        assertNotEquals(task, task3)
    }
}
