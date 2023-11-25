package it.unibo.ai.didattica.competition.tablut.algorithms;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.BoardManager;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.game.MoveHistory;
import it.unibo.ai.didattica.competition.tablut.util.Heuristics;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.LinkedList;
import java.util.Map;

public class AlphaBetaCutoffAlgorithm extends AbstractAlgorithms{
    private final int D;
    private final BoardManager bm;
    private final Heuristics heuristics;
    private long startingTime;

    //HYPERPARAMETERS
    private final float WHITE_WEIGHTS = 2;
    private final float H1_WEIGHT = 50f; //#PAWNS
    private final float H2_WEIGHT = 50f; //SAFETY
    private final float H3_WEIGHT = 25f; //ESCAPE
    private final float H4_WEIGHT = 2.5f; //BRIDGE

    public AlphaBetaCutoffAlgorithm(Game game, HistoryCommandHandler handler, int maxDepth) {
        super(game, handler);
        D = maxDepth;
        bm = BoardManager.getInstance();
        heuristics = new Heuristics();
    }

    public MyVector[] searchForBestAction(GameState state ){
        startingTime = System.currentTimeMillis();
        player = state.getPlayer();
        float alpha = Integer.MIN_VALUE;
        float beta = Integer.MAX_VALUE;
        MyVector[] bestAction = new MyVector[2];
        float v = 0;

        for(Map.Entry<MyVector, LinkedList<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ){
                if( (System.currentTimeMillis()-startingTime)/1000 > 30 ) return bestAction;

                v = minValue(game.result(state,from,to), alpha, beta, 1);
                handler.undo();
                if( v > alpha ){
                    alpha = v;
                    bestAction[0] = from;
                    bestAction[1] = to;
                }
            }
        }

        System.out.println(v);

        return bestAction;
    }//alphaBetaSearch

    private float maxValue(GameState state, float alpha, float beta, int depth ){
        int utility = game.terminalTest(state);
        if( depth > D || utility != 0 )
            return utility + eval(state);

        if( (System.currentTimeMillis()-startingTime)/1000 > 30 )
            return eval(state);

        float v = Integer.MIN_VALUE;
        for(Map.Entry<MyVector, LinkedList<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                //Loop avoidance
                if( MoveHistory.getInstance().loopMove(from, to) ) continue;

                v = Math.max(v, minValue(game.result(state, from, to), alpha, beta, depth + 1));
                handler.undo();
                if (v >= beta) return v;
                alpha = Math.max(alpha, v);
            }
        }
        return v;
    }//maxValue

    private float minValue(GameState state, float alpha, float beta, int depth){
        int utility = game.terminalTest(state);
        if( depth > D || utility != 0 )
            return utility + eval(state);

        if( (System.currentTimeMillis()-startingTime)/1000 > 30 ) return eval(state);

        float v = Integer.MAX_VALUE;
        for(Map.Entry<MyVector, LinkedList<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                //Loop avoidance
                if( MoveHistory.getInstance().loopMove(from, to) ) continue;

                v = Math.min(v, maxValue(game.result(state, from, to), alpha, beta, depth + 1));
                handler.undo();
                if (v <= alpha) return v;
                beta = Math.min(beta, v);
            }
        }
        return v;
    }//minValue

    private float eval(GameState state) {
        float whitePawnsFraction, bridgePosition;

        //HEURISTIC 1 (PAWNS NUMBER)
        MyVector counts = heuristics.getNumberOfPawns(); //wCount, bCount
        float wCount = counts.x * WHITE_WEIGHTS;
        float bCount = counts.y;
        whitePawnsFraction = wCount / bCount;


        //HEURISTIC 2_3 (KING SAFETY AND ESCAPE VALUE)
        float[] kingSafety_escapeValue = heuristics.computeKingSafetyAndEscapeValue();

        //HEURISTIC 4 (BRIDGE POSITION)
        bridgePosition = heuristics.getBridgeValue();

        //if( h2 == 0 ) return Integer.MIN_VALUE;
        float h = H1_WEIGHT*whitePawnsFraction + H2_WEIGHT*kingSafety_escapeValue[0]
                + H3_WEIGHT*kingSafety_escapeValue[1] + H4_WEIGHT*bridgePosition;

        if( state.getPlayer() == BoardManager.W ) return -h;
        else return h;
    }//eval

    // we set a fixed depth limit "d" so that CUTOFF-TEST(state, depth) returns true for all depth greater than
    // some fixed depth d. (It must also return true for all terminal states, just as TERMINAL-TEST
    /*
    private boolean cutoff_test(GameState state, int currentDepth) {
        return currentDepth > D || game.terminalTest(state) != 0;
    }//currentDepth
    */
}//AlphaBetaCutoffAlgorithm
