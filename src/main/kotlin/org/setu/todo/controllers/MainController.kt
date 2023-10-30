package org.setu.todo.controllers

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.Pane
import org.setu.todo.models.TaskModel
import org.setu.todo.models.TaskType
import org.setu.todo.models.TodoListJsonStore
import org.setu.todo.models.TodoListModel
import org.setu.todo.views.Modals
import java.net.URL
import java.util.*

class MainController : Initializable {
    @FXML
    private lateinit var todoListsView : ListView<TodoListModel>
    @FXML
    private lateinit var tasksListView : ListView<TaskModel>
    @FXML
    private lateinit var taskTitle : Label
    @FXML
    private lateinit var clearAllButton: Button
    @FXML
    private lateinit var resetButton: Button
    @FXML
    private lateinit var todoTitledPane: TitledPane

    @FXML
    private lateinit var backgroundOverlay: Pane

    // Search
    @FXML
    private lateinit var searchTextField: TextField
    @FXML
    private lateinit var sortOptions: ComboBox<String>
    @FXML
    private lateinit var statusFilter: ComboBox<String>
    @FXML
    private lateinit var typeFilter: ComboBox<TaskType>
    @FXML
    private lateinit var priorityFilter: Spinner<Int>

    private lateinit var filteredTasks: FilteredList<TaskModel>
    private lateinit var sortedTasks: SortedList<TaskModel>

    companion object {
        val todoListStore = TodoListJsonStore()
        var selectedList: TodoListModel? = null
        var selectedTask: TaskModel? = null
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // Initialize the controller, set up initial state, load data, etc.
        todoListsView.cellFactory = TodoListCellFactory()
        tasksListView.cellFactory = TaskListCellFactory()

        todoListsView.items = todoListStore.findAll()
        val selectionModel = todoListsView.selectionModel

        todoListsView.items.addListener { c: ListChangeListener.Change<out TodoListModel> ->
            while (c.next()) {
                if (c.wasAdded() || c.wasRemoved() || c.wasUpdated() || c.wasReplaced() || c.wasPermutated()) {
                    println("List changed!")
                    println(todoListStore.findAll().size)

                    todoListsView.refresh()

                    if (todoListsView.items.isEmpty()) {
                        selectionModel.select(null)
                    } else {
                        selectionModel.selectFirst()
                    }
                }
            }
        }

        // Define an event handler for selection changes
        selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                // Update the taskView based on the selected item (newValue)
                taskTitle.text = newValue.name
                tasksListView.items = newValue.tasks
                tasksListView.items.addListener { c: ListChangeListener.Change<out TaskModel> ->
                    while (c.next()) {
                        if (c.wasAdded() || c.wasRemoved() || c.wasUpdated() || c.wasReplaced() || c.wasPermutated()) {
                            println("List changed!")
                            println(todoListStore.findAll().size)

                            filteredTasks = tasksListView.items.filtered { true }
                            sortedTasks = SortedList(filteredTasks)
                            tasksListView.refresh()
                            resetFiltersAndSorts()
                            tasksListView.refresh()
                        }
                    }
                }
                selectedList = newValue
            } else {
                // Handle the case when no item is selected, e.g., clear or hide the task view
                tasksListView.items = null
                selectedList = null
            }
            tasksListView.refresh()
        }

        if (todoListsView.items.isNotEmpty()) {
            selectionModel.selectFirst()
        }

        //-----------//
        // Tooltips  //
        //-----------//
        val clearAllTooltip = Tooltip("Clear All")
        Tooltip.install(clearAllButton, clearAllTooltip)

        val resetTooltip = Tooltip("Reset")
        Tooltip.install(resetButton, resetTooltip)

        //-----------//
        // Search    //
        //-----------//
        typeFilter.items.addAll(TaskType.entries)
        typeFilter.selectionModel.selectFirst()

        sortOptions.items.addAll("None", "Due Date", "Priority", "Status")
        sortOptions.selectionModel.selectFirst()

        statusFilter.items.addAll("Any", "Done", "Not Done")
        statusFilter.selectionModel.selectFirst()

        priorityFilter.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1)

        todoTitledPane.isExpanded = true;
    }

    //---------------//
    // END OF INIT   //
    //---------------//

    //-----------//
    // Buttons   //
    //-----------//
    @FXML
    private fun onAddNewListButton(actionEvent: ActionEvent) {
        todoListStore.create(TodoListModel(0, "New List", FXCollections.observableList(arrayListOf())))
        println((actionEvent.source as Button).parent.scene.toString())
    }

    @FXML
    private fun onAddNewTaskButton(actionEvent: ActionEvent) {
        if (selectedList == null) {
            Modals.genericAlert(Alert.AlertType.WARNING,"Please select a list first!")
            return
        }

        backgroundOverlay.isVisible = true

        val modalStage = Modals.createModal(actionEvent, "addTask.fxml")
        // Add an event listener to the modal dialog's close event
        modalStage.setOnHidden {
            backgroundOverlay.isVisible = false
            println("Modal closed")
        }

        modalStage.showAndWait()
    }

    @FXML
    private fun onClearAllButton(actionEvent: ActionEvent) {
        if (todoListStore.findAll().size == 0) {
            Modals.genericAlert(Alert.AlertType.WARNING,"There are no lists to clear!")
            return
        }
        if (Modals.genericAlert(Alert.AlertType.CONFIRMATION,"Are you sure you want to clear all lists?")) {
            todoListStore.deleteAll()
        }
    }

    @FXML
    private fun onSearchButton() {
        if (selectedList != null) {
            //reset the list view
            tasksListView.items = selectedList!!.tasks

            val searchText = searchTextField.text
            val selectedType = typeFilter.value
            val selectedPriority = priorityFilter.value
            val selectedStatus = statusFilter.value
            val selectedSortOption = sortOptions.value

            filteredTasks = tasksListView.items.filtered { task ->
                val titleMatch = task.title.contains(searchText, ignoreCase = true) || searchText.isEmpty()
                val typeMatch = selectedType == task.type || selectedType == TaskType.Any
                val priorityMatch = task.priority >= selectedPriority
                val statusMatch = (selectedStatus == "Any") ||
                        (selectedStatus == "Done" && task.isDone) ||
                        (selectedStatus == "Not Done" && !task.isDone)

                titleMatch && typeMatch && priorityMatch && statusMatch
            }

            sortedTasks = when (selectedSortOption) {
                "Status" -> {
                    SortedList(filteredTasks, Comparator.comparing { task -> task.isDone })
                }
                "Priority" -> {
                    SortedList(filteredTasks, Comparator.comparing { task -> task.priority })
                }
                "Due Date" -> {
                    SortedList(filteredTasks, Comparator.comparing { task -> task.dueAt })
                }
                else -> {
                    // do nothing
                    SortedList(filteredTasks)
                }
            }

            tasksListView.items = sortedTasks
        } else {
            Modals.genericAlert(Alert.AlertType.WARNING,"Please select a list first!")
        }
    }

    @FXML
    private fun onResetButton(actionEvent: ActionEvent) {
        resetFiltersAndSorts()
        onSearchButton()
    }

    //-----------//
    // Helpers   //
    //-----------//
    private fun resetFiltersAndSorts() {
        searchTextField.text = ""
        typeFilter.selectionModel.selectFirst()
        priorityFilter.valueFactory.value = 1
        statusFilter.selectionModel.selectFirst()
        sortOptions.selectionModel.selectFirst()
    }
}