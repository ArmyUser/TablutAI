package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.ActionCommand;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

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
            . C C B .
            . . C . .
            . . . . .

        The black on the top will move in the cell below
         */

        //configuration
        char[][] board = BoardManager.getInstance().getBoard();
        board[0][3] = BoardManager.E;
        board[0][4] = BoardManager.E;
        board[0][5] = BoardManager.B;

        //Black move
        System.out.println(BoardManager.getInstance());
        var map = BoardManager.getInstance().getPossibleMoves(BoardManager.B);

        for(Map.Entry<MyVector, LinkedList<MyVector>> entry : map.entrySet() ){
            System.out.println(entry.getKey()+": "+entry.getValue()+"\n");
        }
    }//testCitadelsMoves

    public static void main(String[] args) {
        //testThroneKingCapture(); OK!
        //testAdjacentKingCapture(); OK!
        //testGeneralKingCapture(); OK!
        //testCitadelKingCapture(); OK!
        //testDoAndUndo();
        //testCitadelsMoves();
    }//main
}//Testing
