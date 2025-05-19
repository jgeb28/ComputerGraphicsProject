module com.example.jvcamera {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.jvcamera to javafx.fxml;
    exports com.example.jvcamera;
    exports com.example.jvcamera.wallculling;
    opens com.example.jvcamera.wallculling to javafx.fxml;
    exports com.example.jvcamera.wireframe;
    opens com.example.jvcamera.wireframe to javafx.fxml;
}