package com.example.uscismonitor

import com.dustinredmond.fxtrayicon.FXTrayIcon
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.awt.Toolkit

class MainClass : Application() {
    override fun start(container: Stage) {
        Platform.setImplicitExit(false)

        val frame = container.apply {
            initStyle(StageStyle.UTILITY)
            title ="USCIS Tracking"
            opacity = 0.0
            x = -1000.0
            y = -1000.0

            FXTrayIcon(this,MainClass::class.java.getResource("appIcon.png")).apply {
                addExitItem(true)
                show()
            }
            show()
        }

        Stage().apply {
            initOwner(frame)
            initStyle(StageStyle.UNDECORATED)
            isAlwaysOnTop = true
            val screenSize = getScreenSize()
            x = screenSize.first - 300.0
            y = 10.0

            val scene = Scene(FXMLLoader(MainClass::class.java.getResource("mainLayout.fxml")).load())
            this.scene =scene
            show()
        }

    }

    private fun getScreenSize(): Pair<Double,Double> {
        val sz = Toolkit.getDefaultToolkit().screenSize
        return Pair(sz.width.toDouble(), sz.height.toDouble())
    }
}

fun main() {
    Application.launch(MainClass::class.java)
}