package View;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class NewController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    @FXML
    public Button generatemaze;
    public IView view;

    public void setView(IView view) {
        this.view = view;
    }

    public void loadNew()  {
        int rows = Integer.parseInt(textField_mazeRows.getText());
        int cols = Integer.parseInt(textField_mazeColumns.getText());

        if(rows<2 || rows>500 ||cols<2||cols>500)
            view.showAlert("Invalid maze rows or maze columns");
        else {
            view.getFromNew(textField_mazeRows,textField_mazeColumns);
            Stage stage=(Stage)generatemaze.getScene().getWindow();
            stage.close();
        }
    }

}
