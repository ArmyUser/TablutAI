package it.unibo.ai.didattica.competition.tablut.algorithms;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.BoardManager;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.game.MoveHistory;
import it.unibo.ai.didattica.competition.tablut.util.Heuristics;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AlphaBetaCutoffAlgorithm extends AbstractAlgorithms{
    private final int D;
    private final Heuristics heuristics;
    private final BoardManager bm;
    private long startingTime;

    //HYPERPARAMETERS 1 sum
    private float WHITE_WEIGHTS = 2;
    private float PAWNS_NUMBER_WEIGHT = 2f;
    private float KING_SAFETY_WEIGHT = 2f;
    private float KING_ESCAPE_WEIGHT = 1.5f;
    private float BRIDGE_WEIGHT = 2f;
    private float ESCAPE_COEFFICIENT = 1f;
    private float BRIDGE_COEFFICIENT = .5f;
    private float SAFETY_COEFFICIENT = 1f;

    private final int timeout;
    private final byte myPlayer;

    private int bestDepth = Integer.MAX_VALUE;
    private int curDepth = Integer.MAX_VALUE;
    private int n_moves = 0;

    public AlphaBetaCutoffAlgorithm(Game game, HistoryCommandHandler handler, int maxDepth, String player, int timeout) {
        super(game, handler);
        D = maxDepth;
        this.timeout = timeout;
        heuristics = new Heuristics();
        if( player.equalsIgnoreCase("white") ) myPlayer = BoardManager.W;
        else myPlayer = BoardManager.B;
        bm = BoardManager.getInstance();
    }

    /*
    public AlphaBetaCutoffAlgorithm(Game game, HistoryCommandHandler handler, int maxDepth, float whiteWeights,
                                    float h1Wheight, float h2Wheight, float h3Wheight, float h4Wheight,
                                    float escapeCoefficient, float bridgeCoefficient, float safetyCoefficient,
                                    char player) {
        super(game, handler);
        D = maxDepth;
        heuristics = new Heuristics();

        WHITE_WEIGHTS = whiteWeights;
        PAWNS_NUMBER_WEIGHT = h1Wheight;
        KING_SAFETY_WEIGHT = h2Wheight;
        KING_ESCAPE_WEIGHT = h3Wheight;
        BRIDGE_WEIGHT = h4Wheight;
        ESCAPE_COEFFICIENT = escapeCoefficient;
        BRIDGE_COEFFICIENT = bridgeCoefficient;
        SAFETY_COEFFICIENT = safetyCoefficient;
        this.myPlayer = player;
        bm = BoardManager.getInstance();
    }
     */

    public MyVector[] searchForBestAction(GameState state ){
        startingTime = System.currentTimeMillis();
        float alpha = Integer.MIN_VALUE;
        float beta = Integer.MAX_VALUE;
        MyVector[] bestAction = new MyVector[2];
        float v = 0;
        n_moves = 0;

        bestDepth = Integer.MAX_VALUE;
        curDepth = Integer.MAX_VALUE;

        for(Map.Entry<MyVector, HashSet<MyVector>> entry : state.getMoves().entrySet() ){
            MyVector from = entry.getKey();
            for(MyVector to : entry.getValue() ){
                if( (System.currentTimeMillis()-startingTime)/1000 > timeout) return bestAction;

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

        System.out.println(alpha);
        System.out.println(n_moves);

        return bestAction;
    }//alphaBetaSearch

    private float maxValue(GameState state, float alpha, float beta, int depth ){
        n_moves++;
        //TERMINAL TEST STARTS
        int utility = game.terminalTest(state, myPlayer);
        if( utility != 0 ){
            curDepth = depth;
            return utility;
        }
        if( depth > D ){
            return eval();
        }

        if( (System.currentTimeMillis()-startingTime)/1000 > timeout)
            return eval();
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
        n_moves++;

        //TERMINAL TEST STARTS
        int utility = game.terminalTest(state, myPlayer);
        if( utility != 0 ){
            curDepth = depth;
            return utility;
        }
        if( depth > D ){
            return eval();
        }

        if( (System.currentTimeMillis()-startingTime)/1000 > timeout) return eval();
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

    private float eval() {
        float bBridgePosition, wBridgePosition;
        float[] bKingSafety_escapeValue, wKingSafety_escapeValue;
        float bH1 = 0, wH1 = 0;

        //HEURISTIC 1 (WHITE AND BLACK PAWNS NUMBER )
        MyVector counts = heuristics.getNumberOfPawns(); //wCount, bCount
        float wCount = counts.x * WHITE_WEIGHTS;
        float bCount = counts.y;

        //Terminal test
        if( bCount == 0 ){
            if( myPlayer == BoardManager.B ) return -100_000;
            else return 100_000;
        }
        else if( wCount == 0 ){
            if( myPlayer == BoardManager.W ) return -100_000;
        }
        else{
            bH1 = bCount / wCount; //max approaches 6
            wH1 = wCount / bCount; //max approaches 6
        }

        if( bm.getPawn(4,4) == BoardManager.K ){
            PAWNS_NUMBER_WEIGHT = 3f;
            BRIDGE_WEIGHT = 3f;
            BRIDGE_COEFFICIENT = 1f;
            KING_SAFETY_WEIGHT = .5f;
            KING_ESCAPE_WEIGHT = 1f;
            ESCAPE_COEFFICIENT = 1f;
            SAFETY_COEFFICIENT = .5f;
        }
        else{
            PAWNS_NUMBER_WEIGHT = 2f;
            BRIDGE_WEIGHT = 2f;
            BRIDGE_COEFFICIENT = .5f;
            KING_SAFETY_WEIGHT = 2f;
            KING_ESCAPE_WEIGHT = 1.5f;
            ESCAPE_COEFFICIENT = 1f;
            SAFETY_COEFFICIENT = 1f;
        }



        //HEURISTIC 2_3 (KING SAFETY AND ESCAPE VALUE)
        bKingSafety_escapeValue = heuristics.computeKingSafetyAndEscapeValueForBlack(SAFETY_COEFFICIENT,ESCAPE_COEFFICIENT);
        wKingSafety_escapeValue = heuristics.computeKingSafetyAndEscapeValueForWhite(SAFETY_COEFFICIENT,ESCAPE_COEFFICIENT);

        //HEURISTIC 4 (BRIDGE POSITION)
        bBridgePosition = heuristics.getBridgeBlackValue(BRIDGE_COEFFICIENT);
        wBridgePosition = heuristics.getBridgeWhiteValue(BRIDGE_COEFFICIENT);

        float bh = PAWNS_NUMBER_WEIGHT *bH1 + KING_SAFETY_WEIGHT *bKingSafety_escapeValue[0]
                + KING_ESCAPE_WEIGHT *bKingSafety_escapeValue[1] + BRIDGE_WEIGHT *bBridgePosition;

        float wh = PAWNS_NUMBER_WEIGHT *wH1 + KING_SAFETY_WEIGHT *wKingSafety_escapeValue[0]
                + KING_ESCAPE_WEIGHT *wKingSafety_escapeValue[1] + BRIDGE_WEIGHT *wBridgePosition;

        /*
        if( state.getPlayer() == myPlayer ){
            if( myPlayer == BoardManager.B ) return -wh;
            else return -bh;
        }
        else{
            if( myPlayer == BoardManager.B ) return bh;
            else return wh;
        }
        */
        if( myPlayer == BoardManager.B ){
            return bh;
        }
        else{
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
