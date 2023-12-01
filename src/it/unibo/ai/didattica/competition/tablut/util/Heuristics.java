package it.unibo.ai.didattica.competition.tablut.util;

import it.unibo.ai.didattica.competition.tablut.domain.GameState;

import java.util.HashSet;

public class Heuristics {

    public MyVector getNumberOfPawns(GameState state) {
        byte[][] board = state.getBoard();

        int wCount = 0;
        int bCount = 0;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if (board[i][j] == GameState.B) bCount++;
                else if( board[i][j] == GameState.W ) wCount++;
        return new MyVector(wCount,bCount);
    }//getNumberOfPawns

    public MyVector getKingPos(GameState state){
        byte[][] board = state.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if( board[i][j] == GameState.K ) return new MyVector(i,j);
            }
        }
        return null;
    }

    public float[] computeKingSafetyAndEscapeValueForWhite(float safetyCoefficient, float ESCAPE_COEFFICIENT,
                                                           MyVector kingPos, GameState state){
        byte[][] board = state.getBoard();
        HashSet<MyVector> citadels = state.getCitadels();
        float safety = 0;
        float escapeValue = 0;

        //int i = bm.getKingPos().x;
        //int j = bm.getKingPos().y;
        int i = kingPos.x;
        int j = kingPos.y;

        //TOP SAFETY
        int k = i-1;
        while( k > 0 && board[k][j] == GameState.E && !citadels.contains(new MyVector(k,j) ) ) k--;
        if( k == 0 ){
            safety += safetyCoefficient;
            escapeValue += ESCAPE_COEFFICIENT;
        }else if(k>-1 && board[k][j] == GameState.W){
            safety += safetyCoefficient;
        }

        //BOT SAFETY
        k = i+1;
        while( k < 8 && board[k][j] == GameState.E && !citadels.contains(new MyVector(k,j) ) ) k++;
        if( k == 8 ){
            safety += safetyCoefficient;
            escapeValue += ESCAPE_COEFFICIENT;
        }else if(k<9 && board[k][j] == GameState.W){
            safety += safetyCoefficient;
        }

        //RIGHT SAFETY
        k = j+1;
        while( k < 8 && board[i][k] == GameState.E && !citadels.contains(new MyVector(i,k) ) ) k++;
        if( k == 8 ){
            safety += safetyCoefficient;
            escapeValue += ESCAPE_COEFFICIENT;
        }else if(k<9 && board[i][k] == GameState.W){
            safety += safetyCoefficient;
        }

        //LEFT SAFETY
        k = j-1;
        while( k > 0 && board[i][k] == GameState.E && !citadels.contains(new MyVector(i,k) ) ) k--;
        if( k == 0 ){
            safety += safetyCoefficient;
            escapeValue += ESCAPE_COEFFICIENT;
        }else if(k>-1 && board[i][k] == GameState.W){
            safety += safetyCoefficient;
        }

        return new float[]{safety,escapeValue};
    }//computeKingSafetyAndEscapeValueForWhite

    public float[] computeKingSafetyAndEscapeValueForBlack(float safetyCoefficient, float ESCAPE_COEFFICIENT,
                                                           MyVector kingPos, GameState state){
        byte[][] board = state.getBoard();
        HashSet<MyVector> citadels = state.getCitadels();
        float safety = safetyCoefficient*4;
        float escapeValue = ESCAPE_COEFFICIENT*4;

        //int i = bm.getKingPos().x;
        //int j = bm.getKingPos().y;
        int i = kingPos.x;
        int j = kingPos.y;

        //TOP SAFETY
        int k = i-1;
        while( k > 0 && board[k][j] == GameState.E && !citadels.contains(new MyVector(k,j) ) ) k--;
        if( k == 0 ){
            safety -= safetyCoefficient;
            escapeValue -= ESCAPE_COEFFICIENT;
        }else if(k>-1 && board[k][j] == GameState.W){
            safety -= safetyCoefficient;
        }

        //BOT SAFETY
        k = i+1;
        while( k < 8 && board[k][j] == GameState.E && !citadels.contains(new MyVector(k,j) ) ) k++;
        if( k == 8 ){
            safety -= safetyCoefficient;
            escapeValue -= ESCAPE_COEFFICIENT;
        }else if(k<9 && board[k][j] == GameState.W){
            safety -= safetyCoefficient;
        }

        //RIGHT SAFETY
        k = j+1;
        while( k < 8 && board[i][k] == GameState.E && !citadels.contains(new MyVector(i,k) ) ) k++;
        if( k == 8 ){
            safety -= safetyCoefficient;
            escapeValue -= ESCAPE_COEFFICIENT;
        }else if(k<9 && board[i][k] == GameState.W){
            safety -= safetyCoefficient;
        }

        //LEFT SAFETY
        k = j-1;
        while( k > 0 && board[i][k] == GameState.E && !citadels.contains(new MyVector(i,k) ) ) k--;
        if( k == 0 ){
            safety -= safetyCoefficient;
            escapeValue -= ESCAPE_COEFFICIENT;
        }else if(k >-1 && board[i][k] == GameState.W){
            safety -= safetyCoefficient;
        }

        return new float[]{safety,escapeValue};
    }//computeKingSafetyAndEscapeValueForBlack

    public float getBridgeWhiteValue(float bridgeCoefficient, GameState state){
        byte[][] board = state.getBoard();
        float val = bridgeCoefficient*4;

        //if( board[4][4] == BoardManager.K ) bridgeCoefficient = 0.3f;

        if( board[2][3] == GameState.B ) val -= bridgeCoefficient;
        if( board[3][2] == GameState.B ) val -= bridgeCoefficient;
        if( board[1][2] == GameState.B ) val -= bridgeCoefficient;
        if( board[2][1] == GameState.B ) val -= bridgeCoefficient;

        if( board[1][6] == GameState.B ) val -= bridgeCoefficient;
        if( board[2][7] == GameState.B ) val -= bridgeCoefficient;
        if( board[2][5] == GameState.B ) val -= bridgeCoefficient;
        if( board[3][6] == GameState.B ) val -= bridgeCoefficient;

        if( board[6][1] == GameState.B ) val -= bridgeCoefficient;
        if( board[7][2] == GameState.B ) val -= bridgeCoefficient;
        if( board[5][2] == GameState.B ) val -= bridgeCoefficient;
        if( board[6][3] == GameState.B ) val -= bridgeCoefficient;

        if( board[7][6] == GameState.B ) val -= bridgeCoefficient;
        if( board[6][7] == GameState.B ) val -= bridgeCoefficient;
        if( board[6][5] == GameState.B ) val -= bridgeCoefficient;
        if( board[5][6] == GameState.B ) val -= bridgeCoefficient;

        return val;
    }//getBridgeConfig

    public float getBridgeBlackValue(float bridgeCoefficient, GameState state){
        byte[][] board = state.getBoard();
        float val = 0f;

        //if( board[4][4] == BoardManager.K ) bridgeCoefficient = 0.3f;

        if( board[2][3] == GameState.B ) val += bridgeCoefficient;
        if( board[3][2] == GameState.B ) val += bridgeCoefficient;
        if( board[1][2] == GameState.B ) val += bridgeCoefficient;
        if( board[2][1] == GameState.B ) val += bridgeCoefficient;

        if( board[1][6] == GameState.B ) val += bridgeCoefficient;
        if( board[2][7] == GameState.B ) val += bridgeCoefficient;
        if( board[2][5] == GameState.B ) val += bridgeCoefficient;
        if( board[3][6] == GameState.B ) val += bridgeCoefficient;

        if( board[6][1] == GameState.B ) val += bridgeCoefficient;
        if( board[7][2] == GameState.B ) val += bridgeCoefficient;
        if( board[5][2] == GameState.B ) val += bridgeCoefficient;
        if( board[6][3] == GameState.B ) val += bridgeCoefficient;

        if( board[7][6] == GameState.B ) val += bridgeCoefficient;
        if( board[6][7] == GameState.B ) val += bridgeCoefficient;
        if( board[6][5] == GameState.B ) val += bridgeCoefficient;
        if( board[5][6] == GameState.B ) val += bridgeCoefficient;

        return val;
    }//getBridgeConfig
}//Heuristics
