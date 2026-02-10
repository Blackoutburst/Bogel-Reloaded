package dev.blackoutburst.bogel.file

import dev.blackoutburst.bogel.utils.default
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.FileChooser
import javafx.stage.Stage

class FileExplorer : Application() {
    companion object {
        private var primaryStage: Stage? = null
        private var callback: (String?) -> Unit = {}

        fun init() {
            default { launch(FileExplorer::class.java) }
        }

        fun pickFile(name: String, extension: String, callback: (String?) -> Unit) {
            Companion.callback = callback

            Platform.runLater { showOpenDialog(name, extension) }
        }

        fun saveFile(name: String, extension: String, callback: (String?) -> Unit) {
            Companion.callback = callback

            Platform.runLater { showSaveDialog(name, extension) }
        }

        private fun showSaveDialog(name: String, extension: String) {
            val fileChooser = FileChooser()
            fileChooser.title = "Save Project"
            fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter(name, extension))

            val file = fileChooser.showSaveDialog(primaryStage)
            primaryStage!!.close()
            callback(file?.absolutePath)
        }

        private fun showOpenDialog(name: String, extension: String) {
            val fileChooser = FileChooser()
            fileChooser.title = "Select Data File"
            fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter(name, extension))

            val file = fileChooser.showOpenDialog(primaryStage)
            primaryStage!!.close()
            callback(file?.absolutePath)
        }
    }

    override fun start(primaryStage: Stage) {
        Companion.primaryStage = primaryStage
    }
}