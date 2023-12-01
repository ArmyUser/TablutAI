package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.ActionCommand;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.AlphaBetaCutoffPlayer;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.player.QueryPlayer;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.HashSet;

public class GameTablut extends Game{
    public GameTablut(GameState initial, HistoryCommandHandler handler){
        this.initial = initial;
        this.histCmdHandler = handler;
        bm = BoardManager.getInstance();
    }

    @Override
    public HashMap<MyVector, HashSet<MyVector>> actions(byte player) {
        return bm.getPossibleMoves(player);
    }//actions

    @Override
    public GameState result(GameState state, MyVector from, MyVector to) {
        byte currentPlayer = state.getPlayer();
        histCmdHandler.handle(new ActionCommand(from,to,currentPlayer));

        byte nextPlayer = currentPlayer == BoardManager.B ? BoardManager.W : BoardManager.B;
        return new GameState(nextPlayer, actions(nextPlayer));
    }//result

    @Override
    public float utility(GameState state, byte to_move) {
        return 0;
    }

    @Override
    public int terminalTest(GameState state, byte player) {
        //The next player checks if state is terminal, so B & W win conditions are inverted
        boolean isKingCaptured = bm.kingWasCaptured();
        boolean isKingEscaped = bm.kingEscapes();

        if( isKingCaptured ){
            if( player==BoardManager.B ) return 100_000;
            else return -100_000;
        }
        if( isKingEscaped ){
            if( player==BoardManager.B ) return -100_000;
            else return 100_000;
        }

        return 0;
    }//terminalTest

    @Override
    public byte toMove(GameState state) {
        return state.getPlayer();
    }

    public static void main(String[] args) {
        //In order to get different move ordering each new match
        MyVector.initHash();
        HistoryCommandHandler handler = new HistoryCommandHandler();

        GameTablut tablut = new GameTablut(new GameState(BoardManager.W,BoardManager.getInstance().getPossibleMoves(BoardManager.W)), handler);
        int maxDepth = 4;
        Player p1 = new AlphaBetaCutoffPlayer(tablut, handler,maxDepth, "white",60);
        //Player p2 = new QueryPlayer();
        Player p2 = new AlphaBetaCutoffPlayer(tablut, handler,maxDepth, "black",60);
        tablut.play(p1,p2);
    }//main
}//GameTablut
