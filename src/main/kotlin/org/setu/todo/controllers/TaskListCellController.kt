package org.setu.todo.controllers

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import org.setu.todo.models.TaskModel
import org.setu.todo.views.Modals

class TaskListCellController {
    @FXML
    lateinit var checkmarkImage: ImageView
    @FXML
    lateinit var taskLabel: Label
    @FXML
    lateinit var priorityLabel: Label
    @FXML
    lateinit var typeLabel: Label
    @FXML
    lateinit var dueTimeLabel: Label

    private var task: TaskModel? = null

    fun updateItem(task: TaskModel) {
        this.task = task
        taskLabel.text = task.title
        dueTimeLabel.text = task.dueAt
        priorityLabel.text = task.priority.toString()
        if (task.priority == 1 ) {
            priorityLabel.style = "-fx-text-fill: red;"
        } else {
            priorityLabel.style = "-fx-text-fill: black;"
        }

        typeLabel.text = task.type.toString()
        checkIsDone()
    }

    @FXML
    private fun onCheckmarkButton() {
        task?.isDone = !task?.isDone!!
        checkIsDone()
        MainController.todoListStore.saveData()
    }

    @FXML
    private fun onEditTaskButton(actionEvent: ActionEvent) {
        MainController.selectedTask = task

        val overlay = checkmarkImage.scene.lookup("#backgroundOverlay") as Pane
        overlay.isVisible = true

        val modalStage = Modals.createModal(actionEvent, "editTask.fxml")
        modalStage.setOnHidden {
            overlay.isVisible = false
            println("Modal closed")
        }

        modalStage.showAndWait()
    }

    private fun checkIsDone() {
        checkmarkImage.isVisible = task!!.isDone

        if (task!!.isDone) {
            taskLabel.style = "-fx-strikethrough: true;"
        } else {
            taskLabel.style = "-fx-strikethrough: false;"
        }
    }
}