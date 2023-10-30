package org.setu.todo.views

import javafx.fxml.FXMLLoader
import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell
import javafx.scene.layout.Pane
import org.setu.todo.controllers.TaskListCellController
import org.setu.todo.models.TaskModel
import java.io.IOException


class TaskListCell : ListCell<TaskModel>() {
    private val fxmlController = TaskListCellController()
    private var fxmlRoot: Pane? = null

    init {
        try {
            val loader = FXMLLoader(javaClass.getResource("/fxml/taskListCell.fxml"))
            loader.setController(fxmlController)
            fxmlRoot = loader.load() as Pane
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun updateItem(item: TaskModel?, empty: Boolean) {
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