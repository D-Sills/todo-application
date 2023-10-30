package org.setu.todo.controllers

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.stage.Stage
import org.setu.todo.models.TaskType
import org.setu.todo.views.Modals
import java.net.URL
import java.time.LocalDate
import java.util.*

class EditTaskController () : Initializable {
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

        taskTitle.text = MainController.selectedTask!!.title
        taskDueDate.value = LocalDate.parse(MainController.selectedTask!!.dueAt)
        taskTypeChoiceBox.selectionModel.select(MainController.selectedTask!!.type)
        taskPriority.valueFactory.value = MainController.selectedTask!!.priority
    }

    @FXML
    private fun onCloseWindowButton() {
        closeWindowButton.scene.window.hide()
    }

    @FXML
    private fun onEditTaskButton() {
        if (!isValidInput()) {
            return
        }

        MainController.selectedTask!!.title = taskTitle.text
        MainController.selectedTask!!.dueAt = taskDueDate.value.toString()
        MainController.selectedTask!!.type = taskTypeChoiceBox.selectionModel.selectedItem
        MainController.selectedTask!!.priority = taskPriority.value

        MainController.todoListStore.saveData()

        //get the stage
        val stage = closeWindowButton.parent.scene.window as Stage
        val listView = stage.owner.scene.lookup("#tasksListView") as ListView<*>
        listView.refresh()

        closeWindowButton.scene.window.hide()
    }

    @FXML
    private fun onDeleteTaskButton() {
        // get selected list
        MainController.selectedList!!.tasks.removeIf { it.title == taskTitle.text &&
                it.dueAt == taskDueDate.value.toString() }

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