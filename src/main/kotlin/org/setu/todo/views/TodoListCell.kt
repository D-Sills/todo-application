package org.setu.todo.views

import javafx.fxml.FXMLLoader
import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell
import javafx.scene.layout.Pane
import org.setu.todo.controllers.TodoListCellController
import org.setu.todo.models.TodoListModel
import java.io.IOException


class TodoListCell : ListCell<TodoListModel>() {
    private val fxmlController = TodoListCellController()
    private var fxmlRoot: Pane? = null

    init {
        loadFXML()
    }

    private fun loadFXML() {
        try {
            val loader = FXMLLoader(javaClass.getResource("/fxml/todoListCell.fxml"))
            loader.setController(fxmlController)
            fxmlRoot = loader.load() as Pane
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun updateItem(item: TodoListModel?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty || item == null) {
            text = null
            graphic = null
        } else {
            fxmlController.updateItem(item)
            graphic = fxmlRoot
            contentDisplay = ContentDisplay.GRAPHIC_ONLY;
        }
    }
}