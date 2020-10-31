package View;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int row_player =0;
    private int col_player =0;
    private boolean solve=false;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileEndPoint=new SimpleStringProperty();
    StringProperty imageFileStartPoint=new SimpleStringProperty();
    StringProperty imageSuccsess=new SimpleStringProperty();


    //update the members of row and col of player
    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;

        //play the music if the user started on solve maze
        //music of coin
        if(solve==true ){
            Media sound=new Media(new File("src/Resources/media/coin.mp3.mp3").toURI().toString());
            MediaPlayer mediaPlayer=new MediaPlayer(sound);
            mediaPlayer.play();
            mediaPlayer.setAutoPlay(true);
        }


        checkFinish();

        draw();
    }

    private void checkFinish(){
        if(this.row_player==maze.getGoalPosition().getRowIndex()&&this.col_player==maze.getGoalPosition().getColumnIndex()){
            try {
                Image imagestart=new Image(new FileInputStream("src/Resources/Images/Fini.gif")) ;
                ImageView imageView = new ImageView();
                imageView.setImage(imagestart);

                imageView.setFitHeight(500);
                imageView.setFitWidth(700);

                final Stage stage = new Stage();
                final StackPane layout = new StackPane();
                layout.getChildren().add(imageView);
                stage.setScene(new Scene(layout));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        draw();
    }

    public void draw_solution(Solution solution){
        solve=true;
        ArrayList<AState> sol = new ArrayList();
        sol=solution.getSolutionPath();
        for(int i=0;i<sol.size();i++){
            Position val=sol.get(i).getState();
            maze.maze[val.getRowIndex()][val.getColumnIndex()]=5;
        }
        draw();
    }


    public void draw()
    {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.getRows();
            int col = maze.getColumns();
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            double w,h;
            Image wallImage = null;
            Image playerImage = null;
            Image endPoint = null;
            Image stepimage=null;
            Image stepimagewhite=null;

            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
                playerImage = new Image(new FileInputStream("src/Resources/Images/player.jpg"));
                endPoint=new Image(new FileInputStream("src/Resources/Images/End.jpg"));
                stepimage=new Image(new FileInputStream("src/Resources/Images/ground.jpg"));
                Image image2=new Image(new FileInputStream("src/Resources/Images/4e22e4af9121ba1.gif"));
                 stepimagewhite=new Image(new FileInputStream("src/Resources/Images/way.jpg"));

                 //Draw wallimage
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(maze.maze[i][j] == 1) // Wall
                        graphicsContext.drawImage(wallImage,j * cellWidth, i * cellHeight,cellWidth,cellHeight);
                    else if(maze.maze[i][j]==5)//solution
                        graphicsContext.drawImage(stepimage,j * cellWidth, i * cellHeight,cellWidth,cellHeight);
                    else if(maze.maze[i][j]==0)//path
                        graphicsContext.drawImage(stepimagewhite,j * cellWidth, i * cellHeight,cellWidth,cellHeight);

                }
            }
                randomimage(cellHeight,cellWidth,image2,graphicsContext);
                double h_end=maze.getGoalPosition().getRowIndex()*cellHeight;
                double w_end=maze.getGoalPosition().getColumnIndex()*cellWidth;
                graphicsContext.drawImage(endPoint,w_end,h_end,cellWidth,cellHeight);
                graphicsContext.drawImage(playerImage,getCol_player() * cellWidth, getRow_player() * cellHeight,cellWidth,cellHeight);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    //this function init a few wall with " ? " sign of mario
    public void randomimage(double cellHeight ,double cellWidth,Image image,GraphicsContext graphicsContext ){
        Random random=new Random();
        for (int i = 0; i <maze.getRows()+maze.getColumns()/2 ; i++) {
            int ran_row= random.nextInt(maze.getRows());
            int col_ran=random.nextInt(maze.getColumns());
            if(maze.maze[ran_row][col_ran]==1)
                graphicsContext.drawImage(image,col_ran * cellWidth, ran_row * cellHeight,cellWidth,cellHeight);
        }
    }

    public int getRow_player() {
        return row_player;
    }
    public int getCol_player() {
        return col_player;
    }
    public String getImageFileEndPoint() {
        return imageFileEndPoint.get();
    }
    public StringProperty imageFileEndPointProperty() {
        return imageFileEndPoint;
    }
    public void setImageFileEndPoint(String imageFileEndPoint) {
        this.imageFileEndPoint.set(imageFileEndPoint);
    }
    public String getImageFileStartPoint() {
        return imageFileStartPoint.get();
    }
    public StringProperty imageFileStartPointProperty() {
        return imageFileStartPoint;
    }
    public void setImageFileStartPoint(String imageFileStartPoint) {
        this.imageFileStartPoint.set(imageFileStartPoint);
    }
    public String getImageSuccsess() {
        return imageSuccsess.get();
    }
    public StringProperty imageSuccsessProperty() {
        return imageSuccsess;
    }
    public void setImageSuccsess(String imageSuccsess) {
        this.imageSuccsess.set(imageSuccsess);
    }
    public void setRow_player(int row_player) {
        this.row_player = row_player;
    }
    public void setCol_player(int col_player) {
        this.col_player = col_player;
    }
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

}
