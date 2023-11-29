package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.ActionCommand;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.AlphaBetaCutoffPlayer;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class Testing {
    private static void testThroneKingCapture(){
        /*
        Throne neighborhood configuration:
            . B .
            B K B
            . . .
            . B .

        The black below the king will move in the cell upon
         */

        //Throne configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[3][3] = BoardManager.E;
        board[3][4] = BoardManager.B;
        board[3][5] = BoardManager.E;

        board[4][3] = BoardManager.B;
        board[4][5] = BoardManager.B;

        board[5][3] = BoardManager.E;
        board[5][4] = BoardManager.E;
        board[5][5] = BoardManager.E;

        board[6][4] = BoardManager.B;

        //Black move
        BoardManager.getInstance().setPawn(new MyVector(6,4), new MyVector(5,4),BoardManager.B);
    }//testThroneKingCapture

    private static void testAdjacentKingCapture(){
        /*
        Throne neighborhood configuration:
            . . B . . .
            . T K . . B
            . . B . . .

        The black on the right of the king will move in the cell at its left (not capturing) and then
        to the cell at its left again
         */

        //Throne configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[3][4] = BoardManager.E;
        board[3][5] = BoardManager.B;
        board[3][6] = BoardManager.E;

        board[4][4] = BoardManager.T;
        board[4][5] = BoardManager.K;
        board[4][6] = BoardManager.E;
        board[4][7] = BoardManager.E;
        board[4][8] = BoardManager.B;

        board[5][4] = BoardManager.E;
        board[5][5] = BoardManager.B;
        board[5][6] = BoardManager.E;

        //Black move
        BoardManager.getInstance().setPawn(new MyVector(4,8), new MyVector(4,7),BoardManager.B);
        BoardManager.getInstance().setPawn(new MyVector(4,7), new MyVector(4,6),BoardManager.B);
    }//testAdjacentKingCapture

    private static void testGeneralKingCapture(){
        /*
        Throne neighborhood configuration:
            . B . . .
            T . K B .
            . . . . .

        The black on the top will move in the cell below
         */

        //Throne configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[3][4] = BoardManager.E;
        board[3][5] = BoardManager.B;
        board[3][6] = BoardManager.E;
        board[3][7] = BoardManager.E;

        board[4][4] = BoardManager.T;
        board[4][5] = BoardManager.E;
        board[4][6] = BoardManager.K;
        board[4][7] = BoardManager.B;

        board[5][4] = BoardManager.E;
        board[5][5] = BoardManager.E;
        board[5][6] = BoardManager.E;
        board[5][7] = BoardManager.E;

        //Black move
        BoardManager.getInstance().setPawn(new MyVector(3,5), new MyVector(4,5),BoardManager.B);
    }//testGeneralKingCapture

    private static void testCitadelKingCapture(){
        /*
        Throne neighborhood configuration:
            . B . . .
            T . K C .
            . . . . .

        The black on the top will move in the cell below
         */

        //Throne configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[3][4] = BoardManager.E;
        board[3][5] = BoardManager.B;
        board[3][6] = BoardManager.E;
        board[3][7] = BoardManager.E;

        board[4][4] = BoardManager.T;
        board[4][5] = BoardManager.E;
        board[4][6] = BoardManager.K;
        board[4][7] = BoardManager.E; //Only row changed

        board[5][4] = BoardManager.E;
        board[5][5] = BoardManager.E;
        board[5][6] = BoardManager.E;
        board[5][7] = BoardManager.E;

        //Black move
        BoardManager.getInstance().setPawn(new MyVector(3,5), new MyVector(4,5),BoardManager.B);
    }//testCitadelKingCapture

    private static void testDoAndUndo(){
        /*
        Throne neighborhood configuration:
            . B . . .
            T . W B .
            . . . . .

        The black on the top will move in the cell below
         */

        //Throne configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[3][4] = BoardManager.E;
        board[3][5] = BoardManager.B;
        board[3][6] = BoardManager.E;
        board[3][7] = BoardManager.E;

        board[4][4] = BoardManager.T;
        board[4][5] = BoardManager.E;
        board[4][6] = BoardManager.W;
        board[4][7] = BoardManager.B;

        board[5][4] = BoardManager.E;
        board[5][5] = BoardManager.E;
        board[5][6] = BoardManager.E;
        board[5][7] = BoardManager.E;

        //Black move
        HistoryCommandHandler handler = new HistoryCommandHandler();

        handler.handle(new ActionCommand(new MyVector(3,5), new MyVector(4,5), BoardManager.B));
        System.out.println(BoardManager.getInstance());

        handler.undo();
        System.out.println(BoardManager.getInstance());
    }//testCitadelKingCapture

    private static void testKingEscape(){
        /*
        Throne neighborhood configuration:
            . . . . .
            E . K . .
            . . . . .

        The black on the top will move in the cell below
         */

        //Throne configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[1][1] = BoardManager.E;
        board[1][2] = BoardManager.K;

        //Black move
        HistoryCommandHandler handler = new HistoryCommandHandler();

        handler.handle(new ActionCommand(new MyVector(1,2), new MyVector(1,0), BoardManager.W));
        System.out.println(BoardManager.getInstance());

        handler.undo();
        System.out.println(BoardManager.getInstance());
    }//testCitadelKingCapture

    private static void testCitadelsMoves(){
        /*
        Throne neighborhood configuration:
            . C B C .
            . . C . .
            . . . . .

        The black on the top will move in the cell below
         */

        //configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[0][3] = BoardManager.E;
        board[0][4] = BoardManager.B;
        board[0][5] = BoardManager.E;

        //Black move
        System.out.println(BoardManager.getInstance());
        var map = BoardManager.getInstance().getPossibleMoves(BoardManager.B);

        for(Map.Entry<MyVector, HashSet<MyVector>> entry : map.entrySet() ){
            System.out.println(entry.getKey()+": "+entry.getValue()+"\n");
        }
    }//testCitadelsMoves

    /*
    private static boolean exec(float whiteWeights, float h1Wheight, float h2Wheight,
                               float h3Wheight, float h4Wheight, float escapeCoefficient,
                                float bridgeCoefficient, float safetyCoefficient){

        int maxDepth = 4;
        BoardManager.getInstance().resetBoard();
        GameTablut tablut = new GameTablut(new GameState(BoardManager.W,BoardManager.getInstance().getPossibleMoves(BoardManager.W)));

        Player p1 = new AlphaBetaCutoffPlayer(tablut, tablut.histCmdHandler,maxDepth,
                whiteWeights, h1Wheight, h2Wheight, h3Wheight, h4Wheight, escapeCoefficient,
                bridgeCoefficient, safetyCoefficient, BoardManager.W);


        Player p2 = new AlphaBetaCutoffPlayer(tablut, tablut.histCmdHandler,maxDepth,
                whiteWeights, h1Wheight, h2Wheight, h3Wheight, h4Wheight, escapeCoefficient,
                bridgeCoefficient, safetyCoefficient, BoardManager.B);


        return tablut.play(p1,p2);
    }//exec

    private static HashMap<Boolean,LinkedList<Float>> tune(){
        HashMap<Boolean,LinkedList<Float>> results = new HashMap<>();
        float whiteWeights, h1Wheight, h2Wheight, h3Wheight, h4Wheight, escapeCoefficient, bridgeCoefficient, safetyCoefficient;

        for( float i=0.05f; i<0.7f; i+=0.05f ){
            for( float j=i+0.05f; j<1-0.25f; j+=0.05f ){
                for( float k=j+0.05f; k<1-0.2f; k+=0.05f ){
                    for( float l=k+0.05f; l<1-0.15f; l+=0.05f ){
                        for( float h=l+0.05f; h<1-0.1f; h+=0.05f ){
                            for( float g=h+0.05f; g<1-0.05f; g+=0.05f ){
                                for( float f=g+0.05f; f<1; f+=0.05f){
                                    whiteWeights = i;
                                    h1Wheight = j-i;
                                    h2Wheight = k-j;
                                    h3Wheight = l-k;
                                    h4Wheight = h-l;
                                    escapeCoefficient = g-h;
                                    bridgeCoefficient = f-g;
                                    safetyCoefficient = 1-f; //H8

                                    boolean winner = exec(
                                            whiteWeights,
                                            h1Wheight,
                                            h2Wheight,
                                            h3Wheight,
                                            h4Wheight,
                                            escapeCoefficient,
                                            bridgeCoefficient,
                                            safetyCoefficient );

                                    LinkedList<Float> params = new LinkedList<>();
                                    params.add(whiteWeights);
                                    params.add(h1Wheight);
                                    params.add(h2Wheight);
                                    params.add(h3Wheight);
                                    params.add(h4Wheight);
                                    params.add(escapeCoefficient);
                                    params.add(bridgeCoefficient);
                                    params.add(safetyCoefficient);

                                    results.put(winner, params);
                                    printTuningResult(winner,params);
                                }//H7
                            }//H6
                        }//H5
                    }//H4
                }//H3
            }//H2
        }//H1

        return results;
    }//tune

    private static void printTuningResults(){
        for( Map.Entry<Boolean, LinkedList<Float>> entry : tune().entrySet())
            printTuningResult(entry.getKey(), entry.getValue());
    }//printTuningResults

    private static void printTuningResult(boolean winner, LinkedList<Float> params){
        char cWinner = winner ? 'W' : 'B';
        System.out.println("Winner: "+cWinner);
        System.out.print("Parameters configuration: ");
        for( float param : params )
            System.out.print(param+", ");

        System.out.println("\n\n\n");
    }//printTuningResult
     */

    public static void main(String[] args) {
        //testThroneKingCapture(); OK!
        //testAdjacentKingCapture(); OK!
        //testGeneralKingCapture(); OK!
        //testCitadelKingCapture(); OK!
        //testDoAndUndo();
        //testCitadelsMoves();
    }//main
}//Testing
