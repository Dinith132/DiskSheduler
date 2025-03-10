module org.example.disksheduler {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.disksheduler to javafx.fxml;
    exports org.example.disksheduler;
}