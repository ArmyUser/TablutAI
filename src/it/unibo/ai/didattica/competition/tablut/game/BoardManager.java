package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.*;

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
    public static final char B = 'B';
    public static final char E = 'E';
    public static final char T = 'T';
    public static final char K = 'K';

    private HashSet<MyVector> citadels;
    private HashSet<MyVector> escapes;

    private boolean isKingCaptured = false;
    private boolean isKingEscaped = false;

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

    public void resetBoard(){
        Instance = new BoardManager();
    }//resetBoard

    protected char board[][];
    protected char turn;

    public char[][] getBoard() {
        return board;
    }

    public HashSet<MyVector> getCitadels(){ return citadels; }

    public String boardString() {
        StringBuilder result = new StringBuilder();
        result.append(" \t");
        for(int i=0; i< board.length; i++)
            result.append(i+" ");

        result.append("\n\n");

        for (int i = 0; i < board.length; i++) {
            result.append(i+"\t");
            for (int j = 0; j < board.length; j++) {
                if( board[i][j] == E ){
                    if( i == 4 && j == 4 ) result.append("⚿ ");
                    else if( citadels.contains(new MyVector(i,j)) ) result.append("■ ");
                    else result.append("□ ");
                }
                else result.append(board[i][j]+" ");
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

    public void setBoard(State state){
        String boardLinearString = state.toLinearString();
        int k = 0;

        for( int i=0; i<board.length; i++ ){
            for (int j = 0; j < board.length; j++) {
                board[i][j] = boardLinearString.charAt(k);
                k++;
            }
        }
    }//setBoard

    public char getTurn() {
        return turn;
    }//getTurn

    public void setTurn(char turn) {
        this.turn = turn;
    }//setTurn

    public HashSet<MyVector> getEscapes(){ return escapes; }

    public LinkedList<MyVector> setPawn(MyVector from, MyVector to, char player){
        if( board[from.x][from.y] == K )
            player = K;
        removePawn(from.x, from.y);
        board[to.x][to.y] = player;

        char opposite = W;
        if( player == B ){
            LinkedList<MyVector> kingPos = captureKing(to);
            if( kingPos.size() > 0 ){
                isKingCaptured = true;
                return kingPos;
            }
        }
        else{
            opposite = B;
            player = W;
        }

        return capture(to,player,opposite);
    }//setPawn

    public void resetPawn(MyVector from, MyVector to, char player){
        if( isKingEscaped ) {
            isKingEscaped = false;
            player = K;
        }

        if( board[from.x][from.y] == K ){
            player = K;
        }

        // remove
        removePawn(from.x, from.y);
        board[to.x][to.y] = player;
    }//resetPawn

    public void respawnPawn(MyVector position, char color){
        if( color == K ){
            isKingEscaped = false;
            isKingCaptured = false;
        }
        if( position != null )
            board[position.x][position.y] = color;
    }//respawnPawn

    private LinkedList<MyVector> capture(MyVector to, char currentPlayer, char opposite){
        LinkedList<MyVector> captured = new LinkedList<>();
        char eventualKing = currentPlayer == W ? K : '.';

        //top capture
        if( to.x > 1 ){
            if( board[to.x-1][to.y] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x-2][to.y] == currentPlayer ||  board[to.x-2][to.y] == eventualKing ||
                        citadels.contains(new MyVector(to.x-2,to.y)) ){
                    removePawn(to.x-1,to.y);
                    captured.add(new MyVector(to.x-1,to.y));
                }
            }
        }

        //bottom capture
        if( to.x < 7 ){
            if( board[to.x+1][to.y] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x+2][to.y] == currentPlayer ||  board[to.x+2][to.y] == eventualKing
                        || citadels.contains(new MyVector(to.x+2,to.y)) ) {
                    removePawn(to.x+1,to.y);
                    captured.add(new MyVector(to.x+1,to.y));
                }
            }
        }

        //right capture
        if( to.y < 7 ){
            if( board[to.x][to.y+1] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x][to.y+2] == currentPlayer ||  board[to.x][to.y+2] == eventualKing
                        || citadels.contains(new MyVector(to.x,to.y+2)) ){
                    removePawn(to.x,to.y+1);
                    captured.add(new MyVector(to.x,to.y+1));
                }
            }
        }

        //left capture
        if( to.y > 1 ){
            if( board[to.x][to.y-1] == opposite ){
                //Captured by 2 allies or by one allies and one citadel (empty throne included)
                if( board[to.x][to.y-2] == currentPlayer ||  board[to.x][to.y-2] == eventualKing
                        || citadels.contains(new MyVector(to.x,to.y-2)) ){
                    removePawn(to.x,to.y-1);
                    captured.add(new MyVector(to.x,to.y-1));
                }
            }
        }
        return captured;
    }//capture

    private LinkedList<MyVector> captureKing(MyVector to){
        LinkedList<MyVector> kingPos = new LinkedList<>();
        //Top king
        if( to.x > 1 ){
            if( board[to.x-1][to.y] == K ){
                kingPos.add(new MyVector(to.x-1,to.y));
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return kingPos;
                }
                else if( isAdjacentToThrone(to.x-1, to.y) ) {
                    if (captureKingAdjacent(to.x-1,to.y)) return kingPos;
                }
                else {
                    if ( capture(to, B, K).size() > 0 ) return kingPos;
                }
            }
        }

        //Bottom king
        if( to.x < 7 ){
            if( board[to.x+1][to.y] == K ){
                kingPos.add(new MyVector(to.x+1,to.y));
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return kingPos;
                }
                else if (isAdjacentToThrone(to.x + 1, to.y)) {
                    if (captureKingAdjacent(to.x+1,to.y)) return kingPos;
                }
                else {
                    if (capture(to, B, K).size() > 0) return kingPos;
                }
            }
        }

        //Right king
        if( to.y < 7 ){
            if( board[to.x][to.y+1] == K ){
                kingPos.add(new MyVector(to.x,to.y+1));
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return kingPos;
                }
                else if( isAdjacentToThrone(to.x, to.y+1) ) {
                    if (captureKingAdjacent(to.x,to.y+1)) return kingPos;
                }
                else {
                    if (capture(to, B, K).size() > 0) return kingPos;
                }
            }
        }

        //Left king
        if( to.y > 1 ){
            if( board[to.x][to.y-1] == K ){
                kingPos.add(new MyVector(to.x,to.y-1));
                if( board[4][4] == K ) {
                    if (captureKingOnThrone()) return kingPos;
                }
                else if( isAdjacentToThrone(to.x, to.y-1) ) {
                    if (captureKingAdjacent(to.x, to.y-1)) return kingPos;
                }
                else {
                    if (capture(to, B, K).size() > 0) return kingPos;
                }
            }
        }

        return new LinkedList<>();
    }//captureKing

    private boolean captureKingOnThrone(){
        return board[3][4] == B && board[5][4] == B && board[4][3] == B && board[4][5] == B;
    }//captureKingOnThrone

    private boolean isAdjacentToThrone(int x, int y){
        return (Math.abs(x-4) == 1 && y == 4) || (Math.abs(y-4) == 1 && x == 4);
    }//isAdjacentToThrone

    private boolean captureKingAdjacent(int x, int y){
        //Black pawn above and below the king
        if( board[x-1][y] == B && board[x+1][y] == B ){
            //Black pawn on the right of the king
            if( board[x][y+1] == B ) return true;
            //Black pawn on the left of the king
            if( board[x][y-1] == B ) return true;
        }
        //Black pawn to the left and to the right of the king
        if( board[x][y-1] == B && board[x][y+1] == B ){
            //Black pawn above the king
            if( board[x-1][y] == B ) return true;
            //Black pawn below the king
            if( board[x+1][y] == B ) return true;
        }
        return false;
    }//captureKingAdjacent

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

    public HashMap<MyVector,HashSet<MyVector>> getPossibleMoves(char currentPlayer) {
        HashMap<MyVector,HashSet<MyVector>> res = new HashMap<>(); //Format: from position -> list of allowed positions
        MyVector thisPos;

        char eventualKing = '.';
        if( currentPlayer == W ) eventualKing = K;

        for( int i=0; i < board.length; i++){
            for( int j=0; j < board.length; j++){
                //If it is the turn of the current player, then
                if( (board[i][j] == currentPlayer || board[i][j] == eventualKing) ){
                    HashSet<MyVector> moves = new HashSet<>();
                    res.put(new MyVector(i,j),moves);

                    //Right side moves
                    int k = j+1;
                    thisPos = new MyVector(i,k);
                    while( k < board.length && board[i][k] == E && !citadels.contains(thisPos) ){
                        moves.add(new MyVector(thisPos));
                        k++;
                        thisPos = new MyVector(i,k);
                    }

                    //Down side moves
                    k = i+1;
                    thisPos = new MyVector(k,j);
                    while( k < board.length && board[k][j] == E && !citadels.contains(thisPos) ){
                        moves.add(new MyVector(thisPos));
                        k++;
                        thisPos = new MyVector(k,j);
                    }

                    //Left side moves
                    k = j-1;
                    thisPos = new MyVector(i,k);
                    while( k > -1 && board[i][k] == E && !citadels.contains(thisPos) ){
                        moves.add(new MyVector(thisPos));
                        k--;
                        thisPos = new MyVector(i,k);
                    }

                    //Top side moves
                    k = i-1;
                    thisPos = new MyVector(k,j);
                    while( k > -1 && board[k][j] == E && !citadels.contains(thisPos) ){
                        moves.add(new MyVector(thisPos));
                        k--;
                        thisPos = new MyVector(k,j);
                    }

                    //If the black pawn is inside a citadel
                    if( citadels.contains(new MyVector(i,j)) && currentPlayer == B ){
                        int l = i-1;
                        while( l > -1 && (l!=4 || j!=4) && board[l][j] == E ){
                            if( i==8 && (j==3 || j==5) && l==0) break;
                            if( !moves.contains(new MyVector(l,j)) )
                                moves.add(new MyVector(l,j));
                            l--;
                        }

                        l = i+1;
                        while( l < 9 && (l!=4 || j!=4) && board[l][j] == E ){
                            if( i==0 && (j==3 || j==5) && l==8) break;
                            if( !moves.contains(new MyVector(l,j)) )
                                moves.add(new MyVector(l,j));
                            l++;
                        }

                        l = j-1;
                        while( l > -1 && (i!=4 || l!=4) && board[i][l] == E ){
                            if( j==8 && (i==3 || i==5) && l==0) break;
                            if( !moves.contains(new MyVector(i,l)) )
                                moves.add(new MyVector(i,l));
                            l--;
                        }

                        l = j+1;
                        while( l < 9 && (i!=4 || l!=4) && board[i][l] == E ){
                            if( j==0 && (i==3 || i==5) && l==8) break;
                            if( !moves.contains(new MyVector(i,l)) )
                                moves.add(new MyVector(i,l));
                            l++;
                        }
                    }
                }//if
            }//for2
        }//for1
        return res;
    }//getPossibleMoves

    private <E> void shuffleMoves(LinkedList<E> moves){
        Random rand = new Random();
        for( int i = 0; i<moves.size(); i++){
            int r = rand.nextInt(moves.size());
            E temp =  moves.get(i);
            moves.set(i, moves.get(r));
            moves.set(r, temp);
        }
    }//shuffleMoves

    public boolean kingEscapes() {
        for( MyVector pos : escapes ) {
            if (board[pos.x][pos.y] == K) {
                isKingEscaped = true;
                return true;
            }
        }
        return false;
    }//kingReachesJolly

    public boolean kingWasCaptured() {
        /*
        if( isKingCaptured ){
            isKingCaptured = false;
            return true;
        }
        return false;
        */
         return isKingCaptured;
    }//kingWasCaptured

    public boolean kingWasEscaped(){ return isKingEscaped; }

    public boolean allPawnsCaptured() {
        for( int i = 0; i<board.length; i++)
            for( int j = 0; j<board.length; j++)
                if( board[i][j] == B) {
                    return false;
                }
        return true;
    }//allPawnsCaptured

    public boolean illegalState(){
        int kcount = 0;
        for( int i = 0; i<board.length; i++)
            for( int j = 0; j<board.length; j++) {
                if (i != 4 && j != 4 && citadels.contains(new MyVector(i, j)) && (board[i][j] == K || board[i][j] == W)) {
                    return true;
                }
                if (i == 4 && j == 4 && board[i][j] == W ){ return true; }
                if( board[i][j] == K ){
                    kcount++;
                    if( kcount > 1 ) return true;
                }
            }
        return false;
    }

    public void printPawnNumber(){
        int wCount = 0;
        int bCount = 0;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if (board[i][j] == BoardManager.B) bCount++;
                else if( board[i][j] == BoardManager.W ) wCount++;
        System.out.println("White pawns: "+wCount);
        System.out.println("Black pawns: "+bCount);
    }
}//BoardManager
