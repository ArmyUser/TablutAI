package it.unibo.ai.didattica.competition.tablut.util;

import it.unibo.ai.didattica.competition.tablut.game.BoardManager;

import java.util.HashSet;

public class Heuristics {
    private final BoardManager bm;

    public Heuristics(){
        bm = BoardManager.getInstance();
    }

    public MyVector getNumberOfPawns() {
        char[][] board = bm.getBoard();

        int wCount = 0;
        int bCount = 0;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if (board[i][j] == BoardManager.B) bCount++;
                else if( board[i][j] == BoardManager.W ) wCount++;
        return new MyVector(wCount,bCount);
    }//getNumberOfPawns

    public float[] computeKingSafetyAndEscapeValue(){
        char[][] board = bm.getBoard();
        HashSet<MyVector> citadels = bm.getCitadels();
        float safety = 0;
        float escapeValue = 0;

        int i = 0;
        int j = 0;
        while( i < 8 ){
            j = 0;
            while( j < 8 && board[i][j] != BoardManager.K ) j++;
            if( board[i][j] == BoardManager.K ) break;
            i++;
        }

        //KING CAPTURED
        if( i == 9 && j == 9) return new float[]{-10,-10};

        //TOP SAFETY
        int k = i-1;
        while( k > 0 && board[k][j] == BoardManager.E && !citadels.contains(new MyVector(k,j) ) ) k--;
        if( k == -1 ){
            safety += 2.5f;
            escapeValue += 2.5f;
        }else if(board[k][j] == BoardManager.W){
            safety += 2.5f;
        }

        //BOT SAFETY
        k = i+1;
        while( k < 8 && board[k][j] == BoardManager.E && !citadels.contains(new MyVector(k,j) ) ) k++;
        if( k == 9 ){
            safety += 2.5f;
            escapeValue += 2.5f;
        }else if(board[k][j] == BoardManager.W){
            safety += 2.5f;
        }

        //RIGHT SAFETY
        k = j+1;
        while( k < 8 && board[i][k] == BoardManager.E && !citadels.contains(new MyVector(i,k) ) ) k++;
        if( k == 9 ){
            safety += 2.5f;
            escapeValue += 2.5f;
        }else if(board[i][k] == BoardManager.W){
            safety += 2.5f;
        }

        //LEFT SAFETY
        k = j-1;
        while( k > 0 && board[i][k] == BoardManager.E && !citadels.contains(new MyVector(i,k) ) ) k--;
        if( k == -1 ){
            safety += 2.5f;
            escapeValue += 2.5f;
        }else if(board[i][k] == BoardManager.W){
            safety += 2.5f;
        }

        return new float[]{safety,escapeValue};
    }//computeKingSafety

    public float getBridgeValue(){
        char[][] board = bm.getBoard();
        float val = 4;

        if( board[2][3] == BoardManager.B ) val -= 0.25f;
        if( board[3][2] == BoardManager.B ) val -= 0.25f;
        if( board[1][2] == BoardManager.B ) val -= 0.25f;
        if( board[2][1] == BoardManager.B ) val -= 0.25f;

        if( board[1][6] == BoardManager.B ) val -= 0.25f;
        if( board[2][7] == BoardManager.B ) val -= 0.25f;
        if( board[2][5] == BoardManager.B ) val -= 0.25f;
        if( board[3][6] == BoardManager.B ) val -= 0.25f;

        if( board[6][1] == BoardManager.B ) val -= 0.25f;
        if( board[7][2] == BoardManager.B ) val -= 0.25f;
        if( board[5][2] == BoardManager.B ) val -= 0.25f;
        if( board[6][3] == BoardManager.B ) val -= 0.25f;

        if( board[7][6] == BoardManager.B ) val -= 0.25f;
        if( board[6][7] == BoardManager.B ) val -= 0.25f;
        if( board[6][5] == BoardManager.B ) val -= 0.25f;
        if( board[5][6] == BoardManager.B ) val -= 0.25f;

        return val;
    }//getBridgeConfig
}//Heuristics
