package View;

import Model.IModel;
import Server.Server;
import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import Model.Model;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class startPatController implements Initializable {
    @FXML
     private AnchorPane rootpane;
        Stage mainstage=new Stage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
@FXML
    public void LoadMyView(javafx.event.ActionEvent actionEvent)throws IOException {
    Media sound=new Media(new File("src/Resources/media/Super Mario Bros. Original Theme by Nintendo.mp3.crdownload").toURI().toString());
    MediaPlayer mediaPlayer=new MediaPlayer(sound);
    mediaPlayer.play();
    mediaPlayer.setAutoPlay(true);
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
    Stage stage=(Stage)rootpane.getScene().getWindow();
    stage.close();
    Parent root = fxmlLoader.load();
    mainstage.setTitle("Maze");
    Scene scene=new Scene(root, 900, 900);
    mainstage.setScene(scene);

    IModel model = new Model();
    MyViewModel viewModel = new MyViewModel(model);
    MyViewController view=fxmlLoader.getController();
    view.setViewModel(viewModel);
    viewModel.addObserver(view);
    mainstage.show();

    }



}
