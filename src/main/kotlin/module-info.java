module com.example.uscismonitor {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core.jvm;
    requires java.desktop;
    requires FXTrayIcon;
    requires org.json;


    opens com.example.uscismonitor to javafx.fxml;
    exports com.example.uscismonitor;
}