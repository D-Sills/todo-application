package org.setu.todo.models

import com.google.gson.reflect.TypeToken
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.hildan.fxgson.FxGson
import org.setu.todo.helpers.exists
import org.setu.todo.helpers.logger
import org.setu.todo.helpers.read
import org.setu.todo.helpers.write
import java.lang.reflect.Type


const val JSON_FILE = "todoLists.json"
var gsonBuilder = FxGson.create()

val listType: Type? = object : TypeToken<ObservableList<TodoListModel>>() {}.type

fun generateRandomId(): Long {
    return (0..Long.MAX_VALUE).random()
}

class TodoListJsonStore : TodoListStore {
    private var todoLists: ObservableList<TodoListModel> = FXCollections.observableArrayList()

    init {
        if (exists(JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): ObservableList<TodoListModel> {
        return todoLists
    }

    override fun findOne(id: Long): TodoListModel? {
        return todoLists.find { t -> t.id == id }
    }

    override fun create(todoList: TodoListModel) {
        todoList.id = generateRandomId()
        todoLists.add(todoList)
        serialize()
    }

    override fun update(todoList: TodoListModel) {
        val foundList = findOne(todoList.id)
        if (foundList != null) {
            foundList.name = todoList.name
            foundList.tasks = todoList.tasks
        }
        serialize()
    }

    override fun delete(todoList: TodoListModel) {
        todoLists.remove(todoList)
        serialize()
    }

    override fun deleteAll() {
        todoLists.clear()
        serialize()
    }

    internal fun logAll() {
        todoLists.forEach { logger.info("$it") }
    }

    private fun serialize() {
        val serializedList = todoLists.map { todoListModel ->
            val taskList = todoListModel.tasks.map { taskModel ->
                mapOf(
                    "id" to taskModel.id,
                    "isDone" to taskModel.isDone,
                    "title" to taskModel.title,
                    "type" to taskModel.type,
                    "priority" to taskModel.priority,
                    "createdAt" to taskModel.createdAt,
                    "dueAt" to taskModel.dueAt
                )
            }
            mapOf(
                "id" to todoListModel.id,
                "name" to todoListModel.name,
                "tasks" to taskList
            )
        }

        val jsonString = gsonBuilder.toJson(serializedList, listType)
        write(JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(JSON_FILE)
        todoLists = gsonBuilder.fromJson(jsonString, listType)
        println("Deserialized todoLists: $todoLists")
    }

    fun saveData() {
        serialize()
    }
}


