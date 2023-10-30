package org.setu.todo.controllers

import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import org.setu.todo.models.TodoListModel
import org.setu.todo.views.Modals

class TodoListCellController {
    @FXML
    private lateinit var listNameTextField: TextField
    @FXML
    private lateinit var listNameLabel: Label
    @FXML
    private lateinit var deleteListButton: Button
    @FXML
    private lateinit var taskAmountLabel: Label

    private var todoList: TodoListModel? = null
    // Add a field to keep track of the TextField's focus state
    private var isEditingListName = false

    fun updateItem(todoList: TodoListModel) {
        this.todoList = todoList
        listNameLabel.text = todoList.name
        taskAmountLabel.text = todoList.tasks.size.toString()
        deleteListButton.isVisible = MainController.todoListStore.findAll().size > 1
    }

    @FXML
    private fun onDeleteListButton() {
        if (Modals.genericAlert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this list?")) {
            todoList?.let { MainController.todoListStore.delete(it) }
        }
    }

    @FXML
    fun editListName(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY && event.clickCount == 2) {
            listNameLabel.isVisible = false
            listNameTextField.isVisible = true
            listNameTextField.text = listNameLabel.text
            listNameTextField.requestFocus()

            // Add a focus listener to the TextField
            listNameTextField.focusedProperty().addListener { _, _, hasFocus ->
                isEditingListName = hasFocus
            }

            listNameTextField.focusedProperty().addListener { _, _, hasFocus ->
                if (!hasFocus && isEditingListName) {
                    saveEditedListName() // Call the save method when focus is lost
                }
            }
        }
    }

    // This method is called when Enter is pressed or the TextField loses focus
    @FXML
    fun saveEditedListName() {
        listNameLabel.text = listNameTextField.text

        listNameLabel.isVisible = true
        listNameTextField.isVisible = false
        if (listNameTextField.text.isEmpty()) {
            Modals.genericAlert(Alert.AlertType.ERROR, "List name cannot be empty!")
            listNameLabel.text = todoList!!.name
            return
        }

        todoList!!.name = listNameTextField.text

        MainController.todoListStore.saveData()

        //get the parent list view and refresh it
        val listview = listNameTextField.parent.scene.lookup("#todoListsView") as ListView<*>
        listview.refresh()

        val taskTitle = listNameTextField.parent.scene.lookup("#taskTitle") as Label
        taskTitle.text = todoList!!.name
    }
}