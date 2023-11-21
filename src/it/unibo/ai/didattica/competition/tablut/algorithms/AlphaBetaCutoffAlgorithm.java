package it.unibo.ai.didattica.competition.tablut.algorithms;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.LinkedList;
import java.util.Map;

public class AlphaBetaCutoffAlgorithm extends AbstractAlgorithms{
    private final int D;

    public AlphaBetaCutoffAlgorithm(Game game, HistoryCommandHandler handler, int maxDepth) {
        super(game, handler);
        D = maxDepth;
    }

    public MyVector[] searchForBestAction(GameState state ){
        player = state.getPlayer();
        float alpha = Integer.MIN_VALUE;
        float beta = Integer.MAX_VALUE;
        MyVector[] bestAction = new MyVector[2];
        float v;

        for(Map.Entry<MyVector, LinkedList<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ){
                v = minValue(game.result(state,from,to), alpha, beta, 1);
                handler.undo();
                if( v > alpha ){
                    alpha = v;
                    bestAction[0] = from;
                    bestAction[1] = to;
                }
            }
        }

        return bestAction;
    }//alphaBetaSearch

    private float maxValue(GameState state, float alpha, float beta, int depth ){
        if( cutoff_test(state, depth) )
            return game.eval(state);

        float v = Integer.MIN_VALUE;
        for(Map.Entry<MyVector, LinkedList<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                v = Math.max(v, minValue(game.result(state, from,to), alpha, beta, depth + 1));
                handler.undo();
                if (v >= beta) return v;
                alpha = Math.max(alpha, v);
            }
        }
        return v;
    }//maxValue

    private float minValue(GameState state, float alpha, float beta, int depth){
        if( cutoff_test(state, depth) )
            return game.eval(state);

        float v = Integer.MAX_VALUE;
        for(Map.Entry<MyVector, LinkedList<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                v = Math.min(v, maxValue(game.result(state, from, to), alpha, beta, depth + 1));
                handler.undo();
                if (v <= alpha) return v;
                beta = Math.min(beta, v);
            }
        }
        return v;
    }//minValue

    // we set a fixed depth limit "d" so that CUTOFF-TEST(state, depth) returns true for all depth greater than
    // some fixed depth d. (It must also return true for all terminal states, just as TERMINAL-TEST
    private boolean cutoff_test(GameState state, int currentDepth) {
        return currentDepth > D || game.terminalTest(state);
    }//currentDepth
}//AlphaBetaCutoffAlgorithm
