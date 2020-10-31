package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;

import algorithms.search.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements IModel{

    private Maze maze;
    private int rowChar;
    private int colChar;
    private Solution solution;
    public Server Server_maze;
    public Server Server_solution;

    public void load_new_maze(int row,int col,Maze maze){
        this.colChar = row;
        this.rowChar = col;
        this.maze=maze;
        ISearchingAlgorithm iSearchingAlgorithm = new BestFirstSearch();
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        solution = iSearchingAlgorithm.solve(searchableMaze);
    }

    public Model() {
        maze = null;
        rowChar =0;
        colChar =0;
        Start_server();
    }

    public void close_server(){
        Server_maze.stop();
        Server_solution.stop();
    }

    public void Start_server(){
         Server_maze = new Server(5400, 1000, new ServerStrategyGenerateMaze());
         Server_solution = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        //Starting  servers
        Server_maze.start();
        Server_solution.start();
    }

    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
         */

        switch(direction)
        {
            case 8: //Up
                if(rowChar!=0 && maze.maze[rowChar-1][colChar]!=1)
                rowChar--;
                break;
            case 2: //Down
                if(rowChar!=maze.getRows()-1 && maze.maze[rowChar+1][colChar]!=1)
                rowChar++;
                break;
            case 4: //Left
                if(colChar!=0 &&  maze.maze[rowChar][colChar-1]!=1)
                colChar--;
                break;
            case 6: //Right
                if(colChar!=maze.getColumns()-1  &&  maze.maze[rowChar][colChar+1]!=1)
                colChar++;
                break;
            case 9://up-right
                if(rowChar!=0 &&  maze.maze[rowChar-1][colChar+1]!=1 && colChar!=0){
                    rowChar--;
                    colChar++;
                }

                break;
            case 7://up-left
                if(rowChar!=0 && colChar!=0 && maze.maze[rowChar-1][colChar-1]==0){
                    rowChar--;
                    colChar--;
                }
                break;
            case 3://down-right
                if(rowChar!=0 && colChar!=0 && maze.maze[rowChar+1][colChar+1]!=1){
                    rowChar++;
                    colChar++;
                }
                break;
            case 1://down-left
                if(rowChar!=0 && colChar!=0 && maze.maze[rowChar+1][colChar-1]!=1){
                    rowChar++;
                    colChar--;
                }
                break;
        }
        setChanged();
        notifyObservers();
    }



    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }
    @Override
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        solution  = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        ArrayList<AState> mazeSolutionSteps = solution.getSolutionPath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //Solving maze
        setChanged();
        notifyObservers();
    }



    public void generateMaze(int row, int col)
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[row*col+12 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        rowChar=maze.getStartPosition().getRowIndex();
                        colChar=maze.getStartPosition().getColumnIndex();
                        maze.maze[maze.getGoalPosition().getRowIndex()][maze.getGoalPosition().getColumnIndex()]=7;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }
    @Override
    public Solution getSolution() {
        return this.solution;
    }
    public Server getServer_maze() {
        return Server_maze;
    }
    public Server getServer_solution() {
        return Server_solution;
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
}
