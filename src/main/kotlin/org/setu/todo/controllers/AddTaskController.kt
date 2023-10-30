package org.setu.todo.controllers

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import org.setu.todo.models.TaskModel
import org.setu.todo.models.TaskType
import org.setu.todo.views.Modals
import java.net.URL
import java.time.LocalDate
import java.util.*

class AddTaskController : Initializable {
    @FXML
    private lateinit var closeWindowButton: Button
    @FXML
    private lateinit var taskTitle: TextField
    @FXML
    private lateinit var taskDueDate: DatePicker
    @FXML
    private lateinit var taskTypeChoiceBox: ComboBox<TaskType>
    @FXML
    private lateinit var taskPriority: Spinner<Int>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        taskTypeChoiceBox.items.addAll(TaskType.entries.toTypedArray())
        taskTypeChoiceBox.items.remove(TaskType.Any)
        taskTypeChoiceBox.selectionModel.selectFirst()

        taskPriority.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1)
    }

    @FXML
    private fun onCloseWindowButton() {
        closeWindowButton.scene.window.hide()
    }

    @FXML
    private fun onCreateTaskButton() {
        if (!isValidInput()) {
            return
        }
        val task = TaskModel(
            0,
            false,
            taskTitle.text,
            taskTypeChoiceBox.selectionModel.selectedItem,
            taskPriority.value,
            Date().toString(),
            taskDueDate.value.toString()
        )
        // get selected list
        MainController.selectedList!!.tasks.add(task)

        MainController.todoListStore.saveData()

        closeWindowButton.scene.window.hide()
    }

    private fun isValidInput(): Boolean {
        val title = taskTitle.text
        val dueDate = taskDueDate.value
        val priority = taskPriority.value

        // Validate the title
        if (title.isBlank()) {
            println("Title cannot be empty.")
            Modals.genericAlert(Alert.AlertType.WARNING,"Title cannot be empty.")
            return false
        }

        // Validate the due date
        if (dueDate == null || dueDate.isBefore(LocalDate.now())) {
            println("Due date is invalid. Please select a future date.")
            Modals.genericAlert(Alert.AlertType.WARNING,"Due date is invalid. Please select a future date.")
            return false
        }

        // Validate the priority
        if (priority < 1 || priority > 5) {
            println("Priority must be a value between 1 and 5.")
            Modals.genericAlert(Alert.AlertType.WARNING,"Priority must be a value between 1 and 5.")
            return false
        }

        return true
    }

}