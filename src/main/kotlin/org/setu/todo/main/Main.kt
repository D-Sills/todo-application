package org.setu.todo.main

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.util.*

// these classes need to be seperated or javafx doesn't properly work when using gradle. idk why :)
class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(GUI::class.java, *args)
        }
    }
}

class GUI : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/fxml/todoMain.fxml"))
        val scene = Scene(fxmlLoader.load(), 1280.0, 720.0)
        stage.isResizable = false //application isn't responsive so non-resizable
        stage.icons.add(Image(Objects.requireNonNull(Main::class.java.getResourceAsStream("/images/icon.png")))) //set the icon
        stage.title = "To-Do List"
        stage.setScene(scene)
        stage.show()
    }
}