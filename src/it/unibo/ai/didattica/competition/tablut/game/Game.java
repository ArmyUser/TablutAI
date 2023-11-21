package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class Game
{
    protected GameState initial;
    protected HistoryCommandHandler histCmdHandler;

    public abstract HashMap<MyVector,LinkedList<MyVector>> actions(char player);

    public abstract GameState result(GameState state, MyVector from, MyVector to);

    public abstract float utility(GameState state, char to_move);

    public abstract boolean terminalTest(GameState state);

    public abstract char toMove(GameState state);

    public abstract float eval(GameState state);

    public void display(){
        System.out.println(BoardManager.getInstance().toString());
    }//display

    public float play( Player... players){
        System.out.println("Match started...");
        GameState state = initial;
        int t=1;
        while( true ){
            System.out.printf("----------Turn %d----------%n",t);
            for( Player p : players ){
                System.out.printf("---> Player %s%n",state.getPlayer());
                display();
                MyVector[] move = p.getNextAction(state);
                state = result(state, move[0], move[1]);
                histCmdHandler.clearHistory();
                if( terminalTest(state) ){
                    System.out.println("Final board");
                    display();
                    return utility(state, toMove(initial));
                }
            }
            t ++;
        }
    }//play
}//is.game.Game
