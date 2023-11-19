package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class BoardManager
{
    private static BoardManager Instance;

    public static final char WW = '1';
    public static final char BW = '0';
    public static final char D = 'D';

    /**
     * Pawn represents the content of a box in the board
     */
    public static final char W = 'W';
    public static final char B = 'W';
    public static final char E = 'E';
    public static final char T = 'T';
    public static final char K = 'K';

    private HashSet<MyVector> citadels;
    private HashSet<MyVector> escapes;

    private BoardManager(){
        board = new char[9][9];
        citadels = new HashSet<>();
        escapes = new HashSet<>();

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = E;

        this.board[4][4] = K;

        this.board[2][4] = W;
        this.board[3][4] = W;
        this.board[5][4] = W;
        this.board[6][4] = W;
        this.board[4][2] = W;
        this.board[4][3] = W;
        this.board[4][5] = W;
        this.board[4][6] = W;

        this.board[0][3] = B;
        this.board[0][4] = B;
        this.board[0][5] = B;
        this.board[1][4] = B;
        this.board[8][3] = B;
        this.board[8][4] = B;
        this.board[8][5] = B;
        this.board[7][4] = B;
        this.board[3][0] = B;
        this.board[4][0] = B;
        this.board[5][0] = B;
        this.board[4][1] = B;
        this.board[3][8] = B;
        this.board[4][8] = B;
        this.board[5][8] = B;
        this.board[4][7] = B;

        citadels.add(new MyVector(0,3));
        citadels.add(new MyVector(0,4));
        citadels.add(new MyVector(0,5));
        citadels.add(new MyVector(1,4));
        citadels.add(new MyVector(3,8));
        citadels.add(new MyVector(4,8));
        citadels.add(new MyVector(5,8));
        citadels.add(new MyVector(4,7));
        citadels.add(new MyVector(4,1));
        citadels.add(new MyVector(3,0));
        citadels.add(new MyVector(4,0));
        citadels.add(new MyVector(5,0));
        citadels.add(new MyVector(7,4));
        citadels.add(new MyVector(8,3));
        citadels.add(new MyVector(8,4));
        citadels.add(new MyVector(8,5));
        //Throne
        citadels.add(new MyVector(4,4));

        escapes.add(new MyVector(0,1));
        escapes.add(new MyVector(0,2));
        escapes.add(new MyVector(0,6));
        escapes.add(new MyVector(0,7));
        escapes.add(new MyVector(1,0));
        escapes.add(new MyVector(2,0));
        escapes.add(new MyVector(1,8));
        escapes.add(new MyVector(2,8));
        escapes.add(new MyVector(6,0));
        escapes.add(new MyVector(7,0));
        escapes.add(new MyVector(6,8));
        escapes.add(new MyVector(7,8));
        escapes.add(new MyVector(8,1));
        escapes.add(new MyVector(8,2));
        escapes.add(new MyVector(8,6));
        escapes.add(new MyVector(8,7));
    }

    public static BoardManager getInstance(){
        if( Instance == null)
            Instance = new BoardManager();
        return Instance;
    }

    protected char board[][];
    protected char turn;

    public char[][] getBoard() {
        return board;
    }

    public String boardString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                result.append(board[i][j]);
                if (j == 8) result.append("\n");

            }
        }
        return result.toString();
    }//boardString

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        // board
        result.append("");
        result.append(boardString());

        result.append("-");
        result.append("\n");

        // TURN
        result.append(turn);

        return result.toString();
    }//toString

    public String toLinearString() {
        StringBuilder result = new StringBuilder();

        // board
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++)
                result.append(board[i][j]);
        result.append(turn);

        return result.toString();
    }//toLinearString

    public char getPawn(int row, int column) {
        return board[row][column];
    }//getPawn

    public void removePawn(int row, int column) {
        board[row][column] = E;
    }//removePawn

    public void setBoard(char[][] board) {
        this.board = board;
    }//setBoard

    public char getTurn() {
        return turn;
    }//getTurn

    public void setTurn(char turn) {
        this.turn = turn;
    }//setTurn

    public void setPawn(MyVector from, MyVector to, char player){
        removePawn(from.x, from.y);
        board[to.x][to.y] = player;

        char opposite = W;
        if( player == B )
            if( captureKing(to) ) return;
        else opposite = B;

        capture(to,player,opposite);
    }//setPawn

    private boolean capture(MyVector to, char currentPlayer, char opposite){
        boolean captured = false;

        //top capture
        if( to.x > 1 ){
            if( board[to.x-1][to.y] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x-2][to.y] == currentPlayer ||
                        (citadels.contains(new MyVector(to.x-2,to.y)) && (board[to.x-2][to.y] != K ) ) ){
                    removePawn(to.x+1,to.y);
                    captured = true;
                }
            }
        }

        //bottom capture
        if( to.x < 7 ){
            if( board[to.x+1][to.y] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x+2][to.y] == currentPlayer ||
                        (citadels.contains(new MyVector(to.x+2,to.y)) && (board[to.x+2][to.y] != K ) ) ){
                    removePawn(to.x+1,to.y);
                    captured = true;
                }
            }
        }

        //right capture
        if( to.y < 7 ){
            if( board[to.x][to.y+1] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x][to.y+2] == currentPlayer ||
                        (citadels.contains(new MyVector(to.x,to.y+2)) && (board[to.x][to.y+2] != K ) ) ){
                    removePawn(to.x,to.y+1);
                    captured = true;
                }
            }
        }

        //left capture
        if( to.y > 1 ){
            if( board[to.x][to.y-1] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x][to.y-2] == currentPlayer ||
                        (citadels.contains(new MyVector(to.x,to.y-2)) && (board[to.x][to.y-2] != K ) ) ){
                    removePawn(to.x,to.y-1);
                    captured = true;
                }
            }
        }

        return captured;
    }//capture

    private boolean captureKing(MyVector to){
        //Top king
        if( to.x > 1 ){
            if( board[to.x-1][to.y] == K ){
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return true;
                }
                else if( isAdjacentToThrone(to.x-1, to.y) ) {
                    if (captureKingAdjacent()) return true;
                }
                else {
                    if (capture(to, B, K)) return true;
                }
            }
        }

        //Bottom king
        if( to.x < 7 ){
            if( board[to.x+1][to.y] == K ){
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return true;
                }
                else if (isAdjacentToThrone(to.x + 1, to.y)) {
                    if (captureKingAdjacent()) return true;
                }
                else {
                    if (capture(to, B, K)) return true;
                }
            }
        }

        //Right king
        if( to.y < 7 ){
            if( board[to.x][to.y+1] == K ){
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return true;
                }
                else if( isAdjacentToThrone(to.x, to.y+1) ) {
                    if (captureKingAdjacent()) return true;
                }
                else {
                    if (capture(to, B, K)) return true;
                }
            }
        }

        //Left king
        if( to.y > 1 ){
            if( board[to.x][to.y-1] == K ){
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return true;
                }
                else if( isAdjacentToThrone(to.x, to.y-1) ) {
                    if (captureKingAdjacent()) return true;
                }
                else {
                    if (capture(to, B, K)) return true;
                }
            }
        }

        return false;
    }//captureKing


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.board == null) ? 0 : deepHashCode(board));
        result = prime * result + Character.hashCode(this.turn);
        return result;
    }//hashCode

    private static int deepHashCode(char[][] matrix) {
        int tmp[] = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            tmp[i] = Arrays.hashCode(matrix[i]);
        }
        return Arrays.hashCode(tmp);
    }//deepHashCode

    public String getCellEncoding(int row, int column) {
        String ret;
        char col = (char) (column + 97);
        ret = col + "" + (row + 1);
        return ret;
    }//getCellEncoding

    public BoardManager clone() {
        BoardManager result = new BoardManager();

        char oldboard[][] = this.getBoard();
        char newboard[][] = result.getBoard();

        for (int i = 0; i < this.board.length; i++)
            for (int j = 0; j < this.board[i].length; j++)
                newboard[i][j] = oldboard[i][j];

        result.setBoard(newboard);
        result.setTurn(this.turn);
        return result;
    }//clone


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof BoardManager))
            return false;
        BoardManager other = (BoardManager) obj;
        if (this.board == null) {
            if (other.board != null)
                return false;
        } else {
            if (other.board == null)
                return false;
            if (this.board.length != other.board.length)
                return false;
            if (this.board[0].length != other.board[0].length)
                return false;
            for (int i = 0; i < other.board.length; i++)
                for (int j = 0; j < other.board[i].length; j++)
                    if (this.board[i][j] != other.board[i][j])
                        return false;
        }
        if (this.turn != other.turn)
            return false;
        return true;
    }//equals

    /**
     * Counts the number of checkers of a specific color on the board. Note: the king is not taken into account for white, it must be checked separately
     * @param color The color of the checker that will be counted. It is possible also to use EMPTY to count empty cells.
     * @return The number of cells of the board that contains a checker of that color.
     */
    public int getNumberOf(char color) {
        int count = 0;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if (board[i][j] == color)
                    count++;
        return count;
    }//getNumberOf

    public HashMap<MyVector,LinkedList<MyVector>> getPossibleMoves(GameState state) {
        HashMap<MyVector,LinkedList<MyVector>> res = new HashMap<>(); //Format: from position -> list of allowed positions
        MyVector thisPos;

        for( int i=0; i< board.length; i++){
            for( int j=0; j< board.length; j++){
                //If it is the turn of the current player, then
                if( board[i][j] == state.getPlayer() ){
                    LinkedList<MyVector> moves = new LinkedList<>();
                    res.put(new MyVector(i,j),moves);

                    //Right side moves
                    int k = j+1;
                    thisPos = new MyVector(i,k);
                    while( k < board.length && board[i][k] == E && citadels.contains(thisPos) ){
                        moves.add(thisPos);
                        k++;
                        thisPos = new MyVector(i,k);
                    }

                    //Down side moves
                    k = i+1;
                    thisPos = new MyVector(k,j);
                    while( k < board.length && board[k][j] == E && citadels.contains(thisPos) ){
                        moves.add(thisPos);
                        k++;
                        thisPos = new MyVector(k,j);
                    }

                    //Left side moves
                    k = j-1;
                    thisPos = new MyVector(i,k);
                    while( k > -1 && board[i][k] == E && citadels.contains(thisPos) ){
                        moves.add(thisPos);
                        k--;
                        thisPos = new MyVector(i,k);
                    }

                    //Top side moves
                    k = i-1;
                    thisPos = new MyVector(k,j);
                    while( k > -1 && board[k][j] == E && citadels.contains(thisPos) ){
                        moves.add(thisPos);
                        k--;
                        thisPos = new MyVector(k,j);
                    }
                }//if
            }//for2
        }//for1

        return res;
    }//getPossibleMoves

    public boolean kingEscapes() {
        for( MyVector pos : escapes)
            if( board[pos.x][pos.y] == K )
                return true;
        return false;
    }//kingReachesJolly

    public boolean kingWasCaptured() {
        for( int i = 0; i<board.length; i++)
            for( int j = 0; j<board.length; j++)
                if( board[i][j] == K)
                    return false;
        return true;
    }//kingWasCaptured

    public boolean allPawnsCaptured() {
        for( int i = 0; i<board.length; i++)
            for( int j = 0; j<board.length; j++)
                if( board[i][j] == B)
                    return false;
        return true;
    }//allPawnsCaptured
}//BoardManager
