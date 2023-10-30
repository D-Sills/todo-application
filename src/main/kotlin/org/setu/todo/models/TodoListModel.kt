package org.setu.todo.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList

data class TodoListModel(
    var id: Long,
    var name: String,
    var tasks: ObservableList<TaskModel> = FXCollections.observableArrayList()
)