package it.unibo.ai.didattica.competition.tablut.algorithms;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.BoardManager;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.game.MoveHistory;
import it.unibo.ai.didattica.competition.tablut.util.Heuristics;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashSet;
import java.util.Map;

public class AlphaBetaCutoffAlgorithm extends AbstractAlgorithms{
    private final int D;
    private final Heuristics heuristics;
    private long startingTime;

    //HYPERPARAMETERS
    private float WHITE_WEIGHTS = 2;
    private float H1_WEIGHT = 50f; //#PAWNS
    private float H2_WEIGHT = 50f; //SAFETY
    private float H3_WEIGHT = 25f; //ESCAPE
    private float H4_WEIGHT = 2.5f; //BRIDGE
    private float KING_CAPTURED_WEIGHT = 30f;
    private float ESCAPE_COEFFICIENT = 1f;
    private float BRIDGE_COEFFICIENT = 2.5f;
    private float SAFETY_COEFFICIENT = 2.5f;

    private final int TIME_TRESHOLD = 50;
    private final char myPlayer;

    private int bestDepth = Integer.MAX_VALUE;
    private int curDepth = Integer.MAX_VALUE;
    private int n_moves = 0;

    public AlphaBetaCutoffAlgorithm(Game game, HistoryCommandHandler handler, int maxDepth, char player) {
        super(game, handler);
        D = maxDepth;
        heuristics = new Heuristics();
        this.myPlayer = player;
    }

    public AlphaBetaCutoffAlgorithm(Game game, HistoryCommandHandler handler, int maxDepth, float whiteWeights,
                                    float h1Wheight, float h2Wheight, float h3Wheight, float h4Wheight,
                                    float escapeCoefficient, float bridgeCoefficient, float safetyCoefficient,
                                    char player) {
        super(game, handler);
        D = maxDepth;
        heuristics = new Heuristics();

        WHITE_WEIGHTS = whiteWeights;
        H1_WEIGHT = h1Wheight;
        H2_WEIGHT = h2Wheight;
        H3_WEIGHT = h3Wheight;
        H4_WEIGHT = h4Wheight;
        ESCAPE_COEFFICIENT = escapeCoefficient;
        BRIDGE_COEFFICIENT = bridgeCoefficient;
        SAFETY_COEFFICIENT = safetyCoefficient;
        this.myPlayer = player;
    }

    public MyVector[] searchForBestAction(GameState state ){
        startingTime = System.currentTimeMillis();
        player = state.getPlayer();
        float alpha = Integer.MIN_VALUE;
        float beta = Integer.MAX_VALUE;
        MyVector[] bestAction = new MyVector[2];
        float v = 0;
        bestDepth = Integer.MAX_VALUE;
        curDepth = Integer.MAX_VALUE;

        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ){
                if( (System.currentTimeMillis()-startingTime)/1000 > TIME_TRESHOLD ) return bestAction;

                v = minValue(game.result(state,from,to), alpha, beta, 1);
                handler.undo();
                if( v > alpha ){
                    alpha = v;
                    bestAction[0] = from;
                    bestAction[1] = to;
                }
                if( v == 100_000 && curDepth < bestDepth ){
                    bestDepth = curDepth;
                    bestAction[0] = from;
                    bestAction[1] = to;
                }
            }
        }

        System.out.println(v);
        System.out.println(n_moves);

        return bestAction;
    }//alphaBetaSearch

    private float maxValue(GameState state, float alpha, float beta, int depth ){

        //TERMINAL TEST STARTS
        int utility = game.terminalTest(state, myPlayer);
        if( utility != 0 ){
            curDepth = depth;
            n_moves++;
            return utility;
        }
        if( depth > D ){
            n_moves++;
            return eval(state);
        }

        if( (System.currentTimeMillis()-startingTime)/1000 > TIME_TRESHOLD )
            return eval(state);
        //TERMINAL TEST ENDS

        float v = Integer.MIN_VALUE;
        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                n_moves++;
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

        //TERMINAL TEST STARTS
        int utility = game.terminalTest(state, myPlayer);
        if( utility != 0 ){
            curDepth = depth;
            n_moves++;
            return utility;
        }
        if( depth > D ){
            n_moves++;
            return eval(state);
        }

        if( (System.currentTimeMillis()-startingTime)/1000 > TIME_TRESHOLD ) return eval(state);
        //TERMINAL TEST ENDS

        float v = Integer.MAX_VALUE;
        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ) {
                n_moves++;
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
        float bBridgePosition, wBridgePosition;
        float[] bKingSafety_escapeValue, wKingSafety_escapeValue;

        //HEURISTIC 1 (WHITE AND BLACK PAWNS NUMBER )
        MyVector counts = heuristics.getNumberOfPawns(); //wCount, bCount
        float wCount = counts.x * WHITE_WEIGHTS;
        float bCount = counts.y;

        float bH1 = bCount / wCount;
        float wH1 = wCount / bCount;


        //HEURISTIC 2_3 (KING SAFETY AND ESCAPE VALUE)
        bKingSafety_escapeValue = heuristics.computeKingSafetyAndEscapeValueForBlack(SAFETY_COEFFICIENT,ESCAPE_COEFFICIENT);
        wKingSafety_escapeValue = heuristics.computeKingSafetyAndEscapeValueForWhite(SAFETY_COEFFICIENT,ESCAPE_COEFFICIENT);

        //HEURISTIC 4 (BRIDGE POSITION)
        bBridgePosition = heuristics.getBridgeBlackValue(BRIDGE_COEFFICIENT);
        wBridgePosition = heuristics.getBridgeWhiteValue(BRIDGE_COEFFICIENT);

        float bh = H1_WEIGHT*bH1 + H2_WEIGHT*bKingSafety_escapeValue[0]
                + H3_WEIGHT*bKingSafety_escapeValue[1] + H4_WEIGHT*bBridgePosition;

        float wh = H1_WEIGHT*wH1 + H2_WEIGHT*wKingSafety_escapeValue[0]
                + H3_WEIGHT*wKingSafety_escapeValue[1] + H4_WEIGHT*wBridgePosition;

        if( state.getPlayer() == myPlayer ){
            if( myPlayer == BoardManager.B ) return -wh;
            else return -bh;
        }
        else{
            if( myPlayer == BoardManager.B ) return bh;
            return wh;
        }
    }//eval

    // we set a fixed depth limit "d" so that CUTOFF-TEST(state, depth) returns true for all depth greater than
    // some fixed depth d. (It must also return true for all terminal states, just as TERMINAL-TEST
    /*
    private boolean cutoff_test(GameState state, int currentDepth) {
        return currentDepth > D || game.terminalTest(state) != 0;
    }//currentDepth
    */
}//AlphaBetaCutoffAlgorithm
