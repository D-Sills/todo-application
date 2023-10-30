package org.setu.todo.views

import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.setu.todo.main.Main
import java.io.IOException
import java.util.*

object Modals {
    /**
     * Reusable alert popup
     * @param desc content of the alert
     */
    fun genericAlert(type: Alert.AlertType,desc: String?) : Boolean {
        val alert = Alert(type)
        alert.headerText = desc
        val stage = alert.dialogPane.scene.window as Stage
        val dialogPane = alert.dialogPane
        stage.scene.stylesheets.add(
            Objects.requireNonNull(Main.Companion::class.java.getResource("/styles.css")).toExternalForm()
        )
        dialogPane.styleClass.add("alert")
        alert.showAndWait()
        return alert.result == ButtonType.OK
    }

    /**
     * Switches the scene to the given fxml file in a new window WITHOUT closing the old scene
     * @param actionEvent handler that reacts to the javafx root event
     * @param sceneName name of the fxml screen the user wants to switch to
     */
    @Throws(IOException::class)
    fun createModal(actionEvent: ActionEvent, sceneName: String): Stage {
        val primaryStage = (actionEvent.source as Button).scene.window as Stage

        val newStage = Stage()
        newStage.initModality(Modality.WINDOW_MODAL)
        newStage.initOwner(primaryStage)
        newStage.isResizable = false
        newStage.initStyle(StageStyle.UNDECORATED)

        val resource = Main::class.java.getResource("/fxml/$sceneName")
            ?: throw IOException("FXML file $sceneName not found")

        val fxmlLoader = FXMLLoader(resource)
        val scene = Scene(fxmlLoader.load())

        newStage.scene = scene

        return newStage
    }
}