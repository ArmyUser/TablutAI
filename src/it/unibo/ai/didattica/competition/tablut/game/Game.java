package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaCutoffAlgorithm;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public abstract class Game
{
    protected GameState initial;

    public abstract HashMap<MyVector, HashSet<MyVector>> actions(GameState state);

    public abstract GameState result(GameState state, MyVector from, MyVector to);

    public abstract float utility(GameState state, byte to_move);

    public abstract int terminalTest(GameState state, byte player);

    public abstract byte toMove(GameState state);

    public void display( GameState state ){
        System.out.println(state);
    }//display

    public boolean play( Player... players){
        System.out.println("Match started...");
        GameState state = initial;
        display(state);
        int t=1;
        while( true ){
            System.out.printf("----------Turn %d----------%n",t);
            for( Player p : players ){
                System.out.printf("---> Player %s%n",(char)state.getPlayer());
                MyVector[] move = p.getNextAction(state);
                state = result(state, move[0], move[1]);
                System.out.println("From: "+move[0]);
                System.out.println("To: "+move[1]);
                display(state);
                if( terminalTest(state, GameState.B) != 0 ){
                    System.out.println("Final board");
                    display(state);
                    System.out.println("Number of turns: "+t);
                    return terminalTest(state, GameState.W) > 0; //If so white wins
                }
            }
            t ++;
        }
    }//play
}//is.game.Game
