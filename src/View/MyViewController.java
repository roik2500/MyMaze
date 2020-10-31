package View;

import Model.IModel;
import Model.Model;
import Server.Server;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;
import com.sun.glass.ui.CommonDialogs;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView,Initializable,Observer {

    public Stage mainStage;
    public MyViewModel viewModel;
    private Maze maze;
    private Solution solution;
    private boolean solve;
    @FXML
    public BorderPane Pane;
    @FXML
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label lbl_player_row;
    public Label lbl_player_column;
    public VBox Vbox;
    public MenuBar menuBar;
    public Button GenrateMaze;
    public Button SolveMaze;
    private Boolean startgenerate=false;
    private Server sereverMaze;
    private Server sereverSol;
    private BorderPane main_bord;
    // To know if the user already press generate maze
    //if yes,user sould press file-->new
    private boolean pressGenerate=false;
    private BorderPane pane;
    public Stage new_generate;
    StringProperty update_player_position_row;
    StringProperty update_player_position_col;

    //function for File->Load
    @Override
    @FXML
    public void loadCliked(javafx.event.ActionEvent  event) throws IOException{
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("save the game");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("maze","*.txt"));
        File file= fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(file==null)
            return;
        try{
            FileInputStream filein=new FileInputStream(file);
            ObjectInputStream in=new ObjectInputStream(filein);
            Maze maze=(Maze)in.readObject();

            in.close();
            filein.close();
            mazeDisplayer.drawMaze(maze);
            startgenerate=true;

            //update the location of player with the start poin of maze fro, file
            viewModel.load_new_maze(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex(),maze);

        } catch (Exception e) {
            showAlert("could not save the maze");
        }
    }

    //function for File->open
    @Override
    @FXML
    public void saveCliked(javafx.event.ActionEvent  event) throws IOException{
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("save the game");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("maze","*.txt"));
        File file= fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(file==null)
            return;
        try{
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(maze);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (Exception e) {
            showAlert("could not save the maze");
        }

    }
@Override
    public void initStringPropert(){
        update_player_position_row = new SimpleStringProperty();
        update_player_position_col = new SimpleStringProperty();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStringPropert();
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);
    }

    //generate the maze
    @Override
    public void generateMaze() {
            try{

                int rows = Integer.parseInt(textField_mazeRows.getText());
                int cols = Integer.parseInt(textField_mazeColumns.getText());
                if(rows<2 || rows>500 ||cols<2||cols>500){
                    showAlert("The Size of the Maze is between 2 to 500,\nplease try again!");
                    pressGenerate=false;
                    return;
                }
               solve=false;
                if(pressGenerate==false)
                    pressGenerate=true;
                else {
                    MyMazeGenerator myMazeGenerator=new MyMazeGenerator();
                    Maze maze=myMazeGenerator.generate(rows,cols);
                    mazeDisplayer.drawMaze(maze);
                    mazeDisplayer.set_player_position(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
                    viewModel.load_new_maze(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex(),maze);


                    return;
                }
                       /* showAlert("The Maze is already built!\nif you want to build a new one please press File->New");
                        return;*/

                    pressGenerate=true;
                    startgenerate=true;
                    viewModel.generateMaze(rows,cols);

            } catch (Exception e) {
                showAlert("please insert Only numbers between 2 to 500");
            }
    }

    //this function is responsibility on the zoom with mouseWheel
    @Override
    public void zoom(ScrollEvent event){
        if(event.isControlDown()){
            double change = event.getDeltaY();
            double zoomConst = 1.03;
            if(change < 0){
                zoomConst = 0.97;
            }
            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()*zoomConst);
            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()*zoomConst);
            event.consume();
        }
    }

    //Exist - close ther server and turn off the system
    @Override
    public void exit(){
         sereverMaze.stop();
        sereverSol.stop();
        Stage stage=(Stage)Pane.getScene().getWindow();
        Platform.exit();
        System.exit(0);
        stage.close();
    }
@Override
    public void New() throws IOException{
        //load again
      /*  FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("New.fxml"));
        Parent root = fxmlLoader.load();
        NewController newcontrol=fxmlLoader.getController();
        Stage newwindows= new Stage();
        newwindows.setScene(new Scene(root, 600, 400));
        newcontrol.setView(this);
        newwindows.show();*/

      textField_mazeRows.setText("0");
     textField_mazeColumns.setText("0");
        //start the music
        Media sound=new Media(new File("src/Resources/media/Super Mario Bros. Original Theme by Nintendo.mp3.crdownload").toURI().toString());
        MediaPlayer mediaPlayer=new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlayer.setAutoPlay(true);
    }

    @Override
    public void getFromNew(TextField rows,TextField col){
         setTextField_mazeRows(rows);
        setTextField_mazeColumns(col);

        int row = Integer.parseInt(rows.getText());
        int cols = Integer.parseInt(col.getText());
        MyMazeGenerator myMazeGenerator=new MyMazeGenerator();
//        viewModel.generateMaze(row,cols);
  /*      Maze maze;
        maze=myMazeGenerator.generate(row,cols);
        viewModel.load_new_maze(row,cols,maze);
        //drawMaze();*/

    }


@Override
    public void ShowGameControll(ActionEvent actionEvent){
        try {
            Image imagestart=new Image(new FileInputStream("src/Resources/Images/control.jpg")) ;
            ImageView imageView = new ImageView();
            imageView.setImage(imagestart);
            imageView.setFitHeight(600);
            imageView.setFitWidth(600);
            final Stage stage = new Stage();
            final StackPane layout = new StackPane();
            layout.getChildren().add(imageView);
            stage.setScene(new Scene(layout));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

@Override
    public void Properties(ActionEvent actionEvent){
        try {
            // Image imagestart = new Image(new FileInputStream(System.getProperty("user.dir") + "\\resources\\Images\\AboutImage.png"));
            Image imagestart=new Image(new FileInputStream("src/Resources/Images/prop.jpg")) ;
            ImageView imageView = new ImageView();
            imageView.setImage(imagestart);
            imageView.setFitHeight(Node.BASELINE_OFFSET_SAME_AS_HEIGHT);
            imageView.setFitWidth(1250);
            final Stage stage = new Stage();
            final StackPane layout = new StackPane();
            layout.getChildren().add(imageView);
            stage.setScene(new Scene(layout));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //whats happen when u choose about
    @Override
    public void About(ActionEvent actionEvent) {
        try {
           Image imagestart=new Image(new FileInputStream("src/Resources/Images/about.jpg")) ;
            ImageView imageView = new ImageView();
            imageView.setImage(imagestart);
            imageView.setFitHeight(Node.BASELINE_OFFSET_SAME_AS_HEIGHT);
           imageView.setFitWidth(1250);
            final Stage stage = new Stage();
            final StackPane layout = new StackPane();
            layout.getChildren().add(imageView);
            stage.setScene(new Scene(layout));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //return the solution
    @Override
    public void solveMaze() {
        if(!startgenerate){
            showAlert("Please Click on Generate Maze befor you solve");
        }else {
            solve=true;
            viewModel.solveMaze();
        }
    }

    //Alret function
    @Override
    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    //move the character
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

@Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {
                this.maze = viewModel.getMaze();
            drawMaze();

            int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getCol_player();
                    int rowFromViewModel = viewModel.getRowChar();
                    int colFromViewModel = viewModel.getColChar();

                    if (solve == true )//Solve Maze
                    {
                        this.solution = viewModel.getSolution();
                        mazeDisplayer.draw_solution(solution);
                    }

                    //update the location of player from all the observables
                    set_update_player_position_row(rowFromViewModel + "");
                    set_update_player_position_col(colFromViewModel + "");
                    this.mazeDisplayer.set_player_position(rowFromViewModel, colFromViewModel);

        }

    }
    //draw the maze
    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }
    public void FocusIt() {
        this.mazeDisplayer.requestFocus();
    }
    public Stage getMainStage() {
        return mainStage;
    }
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    //main stage
    public void setMainyStage(Stage primaryStage){
        this.mainStage=primaryStage;
    }
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    //focus the panel while u change the his size
    public void getFocus() {
        mazeDisplayer.requestFocus();
    }

    //update player position
    //row
    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }
    public String get_update_player_position_row() {
        return update_player_position_row.get();
    }
    //col
    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }
    public String get_update_player_position_col() {
        return update_player_position_col.get();
    }
    @Override
    public void setTextField_mazeRows(TextField textField_mazeRows) {
        this.textField_mazeRows.setText(textField_mazeRows.getText()); ;
    }
    @Override
    public void setTextField_mazeColumns(TextField textField_mazeColumns) {
        this.textField_mazeColumns.setText(textField_mazeColumns.getText());
    }
    }


