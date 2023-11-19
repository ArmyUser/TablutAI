package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class Game
{
    protected StateTablut initial;
    protected HistoryCommandHandler histCmdHandler;

    public abstract HashMap<MyVector,LinkedList<MyVector>> actions(GameState state);

    public abstract GameState result(GameState state, MyVector from, MyVector to);

    public abstract float utility(StateTablut state, BoardManager.Player to_move);

    public abstract boolean terminalTest(StateTablut state);

    public abstract BoardManager.Player toMove(StateTablut state);

    public abstract float eval(StateTablut state);

    public void display(){
        BoardManager.getInstance().display();
    }//display

    public float play( Player... players){
        System.out.println("Match started...");
        StateTablut state = initial;
        int t=1;
        while( true ){
            System.out.printf("----------Turn %d----------%n",t);
            for( Player p : players ){
                System.out.printf("---> Player %s%n",state.getTurn());
                display();
                MyVector move = p.getNextAction(state);
                state = result(state, move);
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
