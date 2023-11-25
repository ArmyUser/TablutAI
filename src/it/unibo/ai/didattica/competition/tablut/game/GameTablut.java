package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.ActionCommand;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.AlphaBetaCutoffPlayer;
import it.unibo.ai.didattica.competition.tablut.player.AlphaBetaPlayer;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.player.QueryPlayer;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.LinkedList;

public class GameTablut extends Game{
    public GameTablut(GameState initial){
        this.initial = initial;
        this.histCmdHandler = new HistoryCommandHandler();
        bm = BoardManager.getInstance();
    }

    @Override
    public HashMap<MyVector,LinkedList<MyVector>> actions(char player) {
        return bm.getPossibleMoves(player);
    }//actions

    @Override
    public GameState result(GameState state, MyVector from, MyVector to) {
        char currentPlayer = state.getPlayer();
        histCmdHandler.handle(new ActionCommand(from,to,currentPlayer));

        if( bm.illegalState() ){
            System.exit(-1);
        }

        char nextPlayer = currentPlayer == BoardManager.B ? BoardManager.W : BoardManager.B;
        return new GameState(nextPlayer, actions(nextPlayer));
    }//result

    @Override
    public float utility(GameState state, char to_move) {
        return 0;
    }

    @Override
    public int terminalTest(GameState state) {
        //The next player checks if state is terminal, so B & W win conditions are inverted
        if(state.getPlayer() == BoardManager.W) {
            if (bm.kingWasCaptured()){
                return Integer.MIN_VALUE;
            }
        }
        else if(bm.kingEscapes() || bm.allPawnsCaptured()) return Integer.MAX_VALUE;

        return 0;
    }//terminalTest

    @Override
    public char toMove(GameState state) {
        return state.getPlayer();
    }

    public static void main(String[] args) {
        GameTablut tablut = new GameTablut(new GameState(BoardManager.W,BoardManager.getInstance().getPossibleMoves(BoardManager.W)));
        int maxDepth = 4;
        Player p1 = new AlphaBetaCutoffPlayer(tablut, tablut.histCmdHandler,maxDepth);
        Player p2 = new QueryPlayer();
        //Player p2 = new AlphaBetaCutoffPlayer(tablut, tablut.histCmdHandler,maxDepth);
        //Player p1 = new AlphaBetaPlayer(tablut, tablut.histCmdHandler);
        //Player p2 = new AlphaBetaPlayer(tablut, tablut.histCmdHandler);
        tablut.play(p1,p2);
    }//main
}//GameTablut
