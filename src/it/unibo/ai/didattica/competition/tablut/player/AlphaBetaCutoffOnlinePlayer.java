package it.unibo.ai.didattica.competition.tablut.player;

import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaCutoffAlgorithm;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.game.GameTablut;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.io.IOException;
import java.util.HashMap;

public class AlphaBetaCutoffOnlinePlayer extends OnlinePlayer{
    private HashMap<Integer,String> rowConverter;

    public AlphaBetaCutoffOnlinePlayer(Game game, int maxDepth, String player,
                                 String name, int timeout, String ipAddress ) throws IOException {
        super(new AlphaBetaCutoffAlgorithm(game,maxDepth,player, timeout), player, name, timeout, ipAddress);

        rowConverter = new HashMap<>();
        rowConverter.put(0,"a");
        rowConverter.put(1,"b");
        rowConverter.put(2,"c");
        rowConverter.put(3,"d");
        rowConverter.put(4,"e");
        rowConverter.put(5,"f");
        rowConverter.put(6,"g");
        rowConverter.put(7,"h");
        rowConverter.put(8,"i");
    }

    @Override
    public void run() {
        try {
            declareName();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (this.getPlayer() == GameState.W ) {
            System.out.println("You are player W!");
            while (true) {
                try {
                    GameState state = new GameState(GameState.W, null);

                    //It will read the state in the server format
                    this.read();

                    //Set the board manager with the new board configuration
                    state.setBoard(getCurrentState());

                    //In our adaptation we have to display the board through board manager
                    System.out.println("Current state:");
                    System.out.println(state);

                    //Compute the best move
                    MyVector[] move = getNextAction(state);

                    //Translate the best move in a way readable by server
                    String from = rowConverter.get(move[0].y) + (move[0].x+1);
                    String to = rowConverter.get(move[1].y) + (move[1].x+1);

                    System.out.println("From: "+from);
                    System.out.println("To: "+to);
                    Action action = new Action(from, to, State.Turn.WHITE);

                    //Send the codified action to the server
                    this.write(action);

                    //Read the state modified by my move
                    this.read();

                    //In our adaptation we have to display the board through board manager
                    //System.out.println("Current state:");
                    //System.out.println(bm);

                    //Blocking call until the black does its move
                    System.out.println("Waiting for your opponent move... ");

                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        else{ //Black turn
            System.out.println("You are player " + this.getPlayer() + "!");
            while (true) {
                try {
                    GameState state = new GameState(GameState.B, null);

                    //It will read the state in the server format
                    this.read();

                    //In our adaptation we have to display the board through board manager
                    //System.out.println("Current state:");
                    //System.out.println(bm);

                    //Read the state after white move
                    System.out.println("Waiting for your opponent move... ");
                    this.read();

                    //Set the board manager with the new board configuration
                    state.setBoard(getCurrentState());

                    //In our adaptation we have to display the board through board manager
                    System.out.println("Current state:");
                    System.out.println(state);

                    //Compute the best move
                    MyVector[] move = getNextAction(state);

                    //Translate the best move in a way readable by server
                    String from = rowConverter.get(move[0].y) + (move[0].x+1);
                    String to = rowConverter.get(move[1].y) + (move[1].x+1);
                    System.out.println("From: "+from);
                    System.out.println("To: "+to);
                    Action action = new Action(from, to, State.Turn.BLACK);

                    //Send the codified action to the server
                    this.write(action);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }//run

    public static void main(String[] args) {
        int maxDepth = 4;
        String name = "Tonino's friends";
        OnlinePlayer client = null;

        byte[][] board = new byte[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = GameState.E;

        board[4][4] = GameState.K;

        board[2][4] = GameState.W;
        board[3][4] = GameState.W;
        board[5][4] = GameState.W;
        board[6][4] = GameState.W;
        board[4][2] = GameState.W;
        board[4][3] = GameState.W;
        board[4][5] = GameState.W;
        board[4][6] = GameState.W;

        board[0][3] = GameState.B;
        board[0][4] = GameState.B;
        board[0][5] = GameState.B;
        board[1][4] = GameState.B;
        board[8][3] = GameState.B;
        board[8][4] = GameState.B;
        board[8][5] = GameState.B;
        board[7][4] = GameState.B;
        board[3][0] = GameState.B;
        board[4][0] = GameState.B;
        board[5][0] = GameState.B;
        board[4][1] = GameState.B;
        board[3][8] = GameState.B;
        board[4][8] = GameState.B;
        board[5][8] = GameState.B;
        board[4][7] = GameState.B;

        //In order to get different move ordering each new match
        MyVector.initHash();
        Game game = new GameTablut(
                new GameState(
                        GameState.W,
                        board) );

        try {
            client = new AlphaBetaCutoffOnlinePlayer(game, maxDepth, args[0], name, Integer.parseInt(args[1]), args[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.run();
    }//main
}//AlphaBetaCutoffOnlinePlayer
