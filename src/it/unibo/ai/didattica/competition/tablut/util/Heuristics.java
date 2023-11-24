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

    public int getKingHammingDistance(){
        return 2;
    }//getKingHammingDistance

    public float computeKingSafety(){
        char[][] board = bm.getBoard();
        HashSet<MyVector> citadels = bm.getCitadels();
        float safety = 0;

        int i = 0;
        int j = 0;
        while( i < 8 ){
            j = 0;
            while( j < 8 && board[i][j] != BoardManager.K ) j++;
            if( board[i][j] == BoardManager.K ) break;
            i++;
        }

        //KING CAPTURED
        if( i == 9 && j == 9) return -10;

        //TOP SAFETY
        int k = i-1;
        while( k > 0 && board[k][j] == BoardManager.E && !citadels.contains(new MyVector(k,j) ) ) k--;
        if( k == -1 || board[k][j] == BoardManager.W ) safety += 2.5f;

        //BOT SAFETY
        k = i+1;
        while( k < 8 && board[k][j] == BoardManager.E && !citadels.contains(new MyVector(k,j) ) ) k++;
        if( k == 9 || board[k][j] == BoardManager.W ) safety += 2.5f;

        //RIGHT SAFETY
        k = j+1;
        while( k < 8 && board[i][k] == BoardManager.E && !citadels.contains(new MyVector(i,k) ) ) k++;
        if( k == 9 || board[i][k] == BoardManager.W ) safety += 2.5f;

        //LEFT SAFETY
        k = j-1;
        while( k > 0 && board[i][k] == BoardManager.E && !citadels.contains(new MyVector(i,k) ) ) k--;
        if( k == -1 || board[i][k] == BoardManager.W ) safety += 2.5f;

        return safety;
    }//computeKingSafety
}//Heuristics
