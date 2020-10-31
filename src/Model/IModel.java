package Model;

import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {
    public void generateMaze(int row, int col);
    public Maze getMaze();
    public void updateCharacterLocation(int direction);
    public int getRowChar();
    public int getColChar();
     public void assignObserver(Observer o);
    public void solveMaze();
    public Solution getSolution();
    public void Start_server();

   public Server getServer_maze();
   public Server getServer_solution();
    public void load_new_maze(int row,int col,Maze maze);

    }
