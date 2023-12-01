package it.unibo.ai.didattica.competition.tablut.algorithms;

//import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashSet;
import java.util.Map;

public class MinMaxAlgorithm extends AbstractAlgorithms {

    public MinMaxAlgorithm(Game game ) {
        super(game);
    }

    public MyVector[] searchForBestAction(GameState state){
        player = state.getPlayer();
        return maxBetweenMin(state);
    }//minMaxDecision

    private float maxValue(GameState state){
        if( game.terminalTest(state, GameState.W) != 0 )
            return game.utility(state,player);

        float v = Integer.MIN_VALUE;
        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getPossibleMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                v = Math.max(v, minValue(game.result(state, from, to)));
            }
        }
        return v;
    }//maxValue

    private float minValue(GameState state){
        if(game.terminalTest(state, GameState.W) != 0)
            return game.utility(state, player);

        float v = Integer.MAX_VALUE;
        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getPossibleMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                v = Math.min(v, maxValue(game.result(state, from, to)));
            }
        }
        return v;
    }//minValue

    private MyVector[] maxBetweenMin(GameState state){
        float max = Integer.MIN_VALUE;
        MyVector[] move = new MyVector[2];
        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getPossibleMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                float v = minValue(game.result(state, from, to));
                if (v > max) {
                    max = v;
                    move[0] = from;
                    move[1] = to;
                }
            }
        }
        return move;
    }//map
}//MinMaxAlgorithm
