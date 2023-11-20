package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.util.MyVector;

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

    private static void testCitadelCapture(){
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
    }//testCitadelCapture

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

    public static void main(String[] args) {
        //testThroneKingCapture(); OK!
        //testAdjacentKingCapture(); OK!
        //testGeneralKingCapture(); OK!
        //testCitadelKingCapture(); OK!
    }//main
}//Testing
