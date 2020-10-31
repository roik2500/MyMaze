package View;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.io.IOException;

public interface IView {
    public void loadCliked(javafx.event.ActionEvent  event) throws IOException;
    public void saveCliked(javafx.event.ActionEvent  event) throws IOException;
    public void initStringPropert();
    public void generateMaze();
    public void zoom(ScrollEvent event);
    public void exit();
    public void New() throws IOException;
    public void ShowGameControll(ActionEvent actionEvent);
    public void Properties(ActionEvent actionEvent);
    public void About(ActionEvent actionEvent);
    public void solveMaze();
    public void showAlert(String message);
    public void mouseClicked(MouseEvent mouseEvent);
    public void drawMaze();
    public void setTextField_mazeRows(TextField textField_mazeRows);
    public void setTextField_mazeColumns(TextField textField_maz);
    public void getFromNew(TextField rows,TextField col);




}
