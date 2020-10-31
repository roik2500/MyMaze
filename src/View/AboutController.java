package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    @FXML
    private AnchorPane about;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void loadAbout(ActionEvent actionEvent)throws IOException {
        BorderPane pane= FXMLLoader.load(getClass().getResource("About.fxml"));
        about.getChildren().setAll(pane);
    }

}
