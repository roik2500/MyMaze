package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze maze;







    private int rowChar;
    private int colChar;
    private boolean solve;


    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
    }

    public void load_new_maze(int row,int col,Maze maze){
        this.rowChar = row;
        this.colChar = col;
        this.maze=maze;
        model.load_new_maze(row,col,maze);
    }



    public Maze getMaze() {
        return maze;
    }

    public int getRowChar() {
        return rowChar;
    }
    public int getColChar() {
        return colChar;
    }
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel) {
            //Update location
            this.rowChar = model.getRowChar();
            this.colChar = model.getColChar();
            this.maze = model.getMaze();

            if (solve == true)//Solve Maze
            {
                model.getSolution();
            }

            setChanged();
            notifyObservers();
        }
    }


    public void generateMaze(int row,int col)
    {
        this.model.generateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        int direction = -1;
        switch (keyEvent.getCode()){
            case NUMPAD8://UP
                direction = 8;
                break;
            case NUMPAD2://DOWN
                direction = 2;
                break;
            case NUMPAD4://LEFT
                direction = 4;
                break;
            case NUMPAD6://RIGHT
                direction = 6;
                break;
            case NUMPAD9://UP-RIGHT
                direction=9;
                break;
            case NUMPAD7://UP-LEFT
                direction=7;
            case NUMPAD3://DOWN-RIGHT
                direction=3;
                break;
            case NUMPAD1://DOWN-LEFT
                direction=1;
                break;
        }

        model.updateCharacterLocation(direction);
    }

    public void solveMaze()
    {
         model.solveMaze();

    }

    public Solution getSolution()
    {

        return model.getSolution();
    }
}
