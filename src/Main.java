import Model.IModel;
import Model.Model;
import View.MyViewController;
import View.startPatController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/startPad.fxml"));
          Parent root = fxmlLoader.load();
          primaryStage.setTitle("Maze");
          Scene scene=new Scene(root, 900, 900);
          primaryStage.setScene(scene);
          primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
