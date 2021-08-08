module uk.co.hexillium.maze {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.codehaus.groovy;
    requires javafx.graphics;
    requires java.desktop;

    opens uk.co.hexillium.maze to javafx.fxml;
    exports uk.co.hexillium.maze;
}