package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaCutoffAlgorithm;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public abstract class Game
{
    protected GameState initial;
    protected HistoryCommandHandler histCmdHandler;
    protected BoardManager bm;
    public abstract HashMap<MyVector, HashSet<MyVector>> actions(char player);

    public abstract GameState result(GameState state, MyVector from, MyVector to);

    public abstract float utility(GameState state, char to_move);

    public abstract int terminalTest(GameState state, char player);

    public abstract char toMove(GameState state);

    public void display(){
        System.out.println(bm.toString());
    }//display

    public boolean play( Player... players){
        System.out.println("Match started...");
        GameState state = initial;
        display();
        int t=1;
        while( true ){
            System.out.printf("----------Turn %d----------%n",t);
            for( Player p : players ){
                System.out.printf("---> Player %s%n",state.getPlayer());
                MyVector[] move = p.getNextAction(state);
                state = result(state, move[0], move[1]);
                MoveHistory.getInstance().insertMove(move[0], move[1]);
                bm.printPawnNumber();
                System.out.println("From: "+move[0]);
                System.out.println("To: "+move[1]);
                display();
                histCmdHandler.clearHistory();
                if( terminalTest(state, BoardManager.B) != 0 ){
                    System.out.println("Final board");
                    display();
                    System.out.println("Number of turns: "+t);
                    return terminalTest(state, BoardManager.W) > 0; //If so white wins
                }
            }
            t ++;
        }
    }//play
}//is.game.Game
