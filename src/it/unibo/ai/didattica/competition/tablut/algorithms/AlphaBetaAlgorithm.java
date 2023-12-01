package it.unibo.ai.didattica.competition.tablut.algorithms;

import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashSet;
import java.util.Map;

public class AlphaBetaAlgorithm extends AbstractAlgorithms{

    public AlphaBetaAlgorithm(Game game ) {
        super(game);
    }

    public MyVector[] searchForBestAction(GameState state ){
        player = state.getPlayer();
        float alpha = Integer.MIN_VALUE;
        float beta = Integer.MAX_VALUE;
        MyVector[] bestAction = new MyVector[2];
        float v;

        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getPossibleMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                v = minValue(game.result(state, from, to), alpha, beta);
                if (v > alpha) {
                    alpha = v;
                    bestAction[0] = from;
                    bestAction[1] = to;
                }
            }
        }
        return bestAction;
    }//alphaBetaSearch

    protected float maxValue( GameState state, float alpha, float beta ){
        if( game.terminalTest(state, GameState.W) != 0)
            return game.utility(state,player);

        float v = Integer.MIN_VALUE;
        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getPossibleMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                v = Math.max(v, minValue(game.result(state, from, to), alpha, beta));
                if (v >= beta) return v;
                alpha = Math.max(alpha, v);
            }
        }
        return v;
    }//maxValue

    private float minValue(GameState state, float alpha, float beta){
        if(game.terminalTest(state, GameState.W) != 0)
            return game.utility(state, player);

        float v = Integer.MAX_VALUE;
        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getPossibleMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                v = Math.min(v, maxValue(game.result(state, from, to), alpha, beta));
                if (v <= alpha) return v;
                beta = Math.min(beta, v);
            }
        }
        return v;
    }//minValue
}//AlphaBetaAlgorithm
