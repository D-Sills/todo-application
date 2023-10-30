package org.setu.todo.controllers
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import org.setu.todo.models.TaskModel
import org.setu.todo.views.TaskListCell

class TaskListCellFactory: Callback<ListView<TaskModel>, ListCell<TaskModel>> {
    override fun call(param: ListView<TaskModel>?): ListCell<TaskModel> {
        return TaskListCell()
    }
}

