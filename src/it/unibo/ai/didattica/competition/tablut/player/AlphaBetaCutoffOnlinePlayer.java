package it.unibo.ai.didattica.competition.tablut.player;

import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaCutoffAlgorithm;
import it.unibo.ai.didattica.competition.tablut.command.ActionCommand;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.game.BoardManager;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.game.GameTablut;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class AlphaBetaCutoffOnlinePlayer extends OnlinePlayer{
    private final BoardManager bm;
    private HashMap<Integer,String> rowConverter;
    private HistoryCommandHandler handler;

    public AlphaBetaCutoffOnlinePlayer(Game game, HistoryCommandHandler handler, int maxDepth, String player,
                                 String name, int timeout, String ipAddress ) throws IOException {
        super(new AlphaBetaCutoffAlgorithm(game,handler,maxDepth,player, timeout), player, name, timeout, ipAddress);

        bm = BoardManager.getInstance();
        this.handler = handler;

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

        if (this.getPlayer() == BoardManager.W ) {
            System.out.println("You are player W!");
            while (true) {
                try {
                    //It will read the state in the server format
                    this.read();

                    //Set the board manager with the new board configuration
                    bm.setBoard(getCurrentState());

                    //In our adaptation we have to display the board through board manager
                    System.out.println("Current state:");
                    System.out.println(bm);

                    //Compute the best move
                    HashMap<MyVector, HashSet<MyVector>> moves = bm.getPossibleMoves(BoardManager.W);
                    MyVector[] move = getNextAction(new GameState(BoardManager.W, moves));

                    //Translate the best move in a way readable by server
                    String from = rowConverter.get(move[0].y) + (move[0].x+1);
                    String to = rowConverter.get(move[1].y) + (move[1].x+1);
                    handler.handle(new ActionCommand(move[0], move[1], BoardManager.W));
                    handler.clearHistory();

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
                    //It will read the state in the server format
                    //In this case the
                    this.read();

                    //In our adaptation we have to display the board through board manager
                    //System.out.println("Current state:");
                    //System.out.println(bm);

                    //Read the state after white move
                    System.out.println("Waiting for your opponent move... ");
                    this.read();

                    //Set the board manager with the new board configuration
                    bm.setBoard(getCurrentState());

                    //In our adaptation we have to display the board through board manager
                    System.out.println("Current state:");
                    System.out.println(bm);

                    //Compute the best move
                    HashMap<MyVector, HashSet<MyVector>> moves = bm.getPossibleMoves(BoardManager.B);
                    MyVector[] move = getNextAction(new GameState(BoardManager.B, moves));
                    handler.handle(new ActionCommand(move[0], move[1], BoardManager.W));
                    handler.clearHistory();

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

        //In order to get different move ordering each new match
        MyVector.initHash();
        HistoryCommandHandler handler = new HistoryCommandHandler();
        Game game = new GameTablut(new GameState(BoardManager.W, BoardManager.getInstance().getPossibleMoves(BoardManager.W)), handler);

        try {
            client = new AlphaBetaCutoffOnlinePlayer(game,
                    handler, maxDepth, args[0], name,
                    Integer.parseInt(args[1]), args[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.run();
    }//main
}//AlphaBetaCutoffOnlinePlayer
