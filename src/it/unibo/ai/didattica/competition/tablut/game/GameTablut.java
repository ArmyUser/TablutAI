package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.ActionCommand;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.LinkedList;

public class GameTablut extends Game{

    @Override
    public HashMap<MyVector,LinkedList<MyVector>> actions(GameState state) {
        return BoardManager.getInstance().getPossibleMoves(state);
    }//actions

    @Override
    public GameState result(GameState state, MyVector from, MyVector to) {
        char currentPlayer = state.getPlayer();
        histCmdHandler.handle(new ActionCommand(from,to,currentPlayer));

        char nextPlayer = currentPlayer == BoardManager.B ? BoardManager.W : BoardManager.B;
        return new GameState(nextPlayer,computeUtility(currentPlayer));
    }//result

    @Override
    public float utility(StateTablut state, BoardManager.Player to_move) {
        return 0;
    }

    private float computeUtility(char to_move){
        // winning conditions:
        // - WHITE: if the opponent have finished its pawns
        // - WHITE: if the King reaches jolly positions
        // - BLACK: if its pawns have captured the King

        if(to_move == BoardManager.B)
            if(BoardManager.getInstance().kingWasCaptured()) return -1;

        else if(BoardManager.getInstance().kingEscapes() || BoardManager.getInstance().allPawnsCaptured()) return 1;
        return 0;
    }//computeUtility

    @Override
    public boolean terminalTest(StateTablut state) {
        return false;
    }

    @Override
    public BoardManager.Player toMove(StateTablut state) {
        return null;
    }

    @Override
    public float eval(StateTablut state) {
        return 0;
    }
}//GameTablut
