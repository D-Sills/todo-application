package org.setu.todo.controllers
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import org.setu.todo.models.TodoListModel
import org.setu.todo.views.TodoListCell

class TodoListCellFactory: Callback<ListView<TodoListModel>, ListCell<TodoListModel>> {
    override fun call(param: ListView<TodoListModel>?): ListCell<TodoListModel> {
        return TodoListCell()
    }
}

