package com.example.uscismonitor

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.system.exitProcess

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class MainController:Initializable {
    @FXML
    lateinit var lblI751range: Label
    @FXML
    lateinit var lblN400daysLeft: Label
    @FXML
    lateinit var lblN400date: Label
    @FXML
    lateinit var lblI751daysLeft: Label
    @FXML
    lateinit var lblI751date: Label
    @FXML
    lateinit var btnClose: Button

    //Internet CMD
    private val showInterfaces = "netsh wlan show interfaces".split(" ").toTypedArray()
    private val wifiDevice = "netsh wlan connect name=\"Nami\"".split(" ").toTypedArray()

    private val i751End = LocalDate.of(2021, Month.AUGUST,23)

    private var mouseOffsetX : Double = 0.0
    private var mouseOffsetY : Double = 0.0

    @FXML
    private fun onClose() {
        exitProcess(0)
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        //Check internet connection
        checkInternetConnection()
        requestProcessTime()

    }


    /**
     * Make a http request to USCIS website to retrieve process time of the Form
     */
    private fun requestProcessTime() {
        val url = URL("https://egov.uscis.gov/processing-times/api/processingtime/I-751/CSC")
        var response: String
        (url.openConnection() as HttpsURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Referer", "https://egov.uscis.gov/processing-times/")
            val scanner = Scanner( if (responseCode /100 ==2) inputStream else errorStream).useDelimiter("\\A")
            response = if (scanner.hasNext()) scanner.next() else ""
            disconnect()
        }



        val jsonObject = JSONObject(response)
        val processRange = mutableListOf<Float>()
        val content = jsonObject.getJSONObject("data").getJSONObject("processing_time")
        content.getJSONArray("range").forEach {
                processRange.add((it as JSONObject).getFloat("value"))
        }

        val serviceRequestDate:String = (content.getJSONArray("subtypes").get(0) as JSONObject).getString("service_request_date")
        processRange.sort()

        lblI751range.text = "Range: ${processRange[0]} --- ${processRange[1]}"
        lblI751date.text = "Current: ${serviceRequestDate.uppercase()}"
        val date = serviceRequestDate.replace(",","") .split(" ")
        val i751Current = LocalDate.of(date[2].toInt(), Month.valueOf(date[0].uppercase()),date[1].toInt())
        val i751DaysLeft = ChronoUnit.DAYS.between(i751Current,i751End)
        lblI751daysLeft.text = "$i751DaysLeft days left Until Reviewed"

        val currentN400Date = LocalDate.now().minusDays(1005)
        lblN400date.text = "Current: ${currentN400Date.month} ${currentN400Date.dayOfMonth}, ${currentN400Date.year}"
        val n400RemainingDays = ChronoUnit.DAYS.between(currentN400Date,LocalDate.of(2019,Month.NOVEMBER,8))
        lblN400daysLeft.text = "$n400RemainingDays days left Until filing"

    }



    //Check Network Connection
    /**
     * Check for internet Connection before opening the actual app
     */
    private fun checkInternetConnection() {
        val result = CommonFunction.runCMD(showInterfaces)
        if (result.contains("disconnected") || result.contains("authenticating")){
            showAlert("Please connect to internet to retrieve data. Trying to Connect", Alert.AlertType.WARNING)
            connectToInternet()
        }
    }

    private fun connectToInternet() {
        CommonFunction.runCMD(wifiDevice)
        val stage = Stage(StageStyle.UNDECORATED)
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("progressLayout.fxml")!!
        val scene = Scene(loader.load())
        stage.scene = scene

        CoroutineScope(Dispatchers.Default).launch {
            delay(4000L)
            println("Done")
            withContext(Dispatchers.Main){
                stage.close()
            }
        }
        stage.showAndWait()
        println("continue!!!")
        val testing = CommonFunction.runCMD(showInterfaces)
        if (testing.contains("disconnected") || testing.contains("error") || testing.contains("authenticating")) {
            showAlert("Failed to connect to the internet. Please do it manually.", Alert.AlertType.ERROR)
            exitProcess(0)
        }
    }

    private fun showAlert(content:String, type: Alert.AlertType) = Alert(type).apply {
        title ="No Internet Connection"
        headerText = null
        contentText =content
        showAndWait()
    }

    @FXML
    fun onMouseDragged(mouseEvent: MouseEvent) {
        val stage = btnClose.scene.window as Stage
        stage.x = mouseEvent.screenX + mouseOffsetX
        stage.y = mouseEvent.screenY + mouseOffsetY
    }

    @FXML
    fun onMousePressed(mouseEvent: MouseEvent) {
        val stage = btnClose.scene.window as Stage
        this.mouseOffsetX = stage.x - mouseEvent.screenX
        this.mouseOffsetY = stage.y - mouseEvent.screenY
    }
}