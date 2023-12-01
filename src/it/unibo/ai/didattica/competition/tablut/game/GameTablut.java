package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.AlphaBetaCutoffPlayer;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.HashSet;

public class GameTablut extends Game{
    public GameTablut(GameState initial ){
        this.initial = initial;
    }

    @Override
    public HashMap<MyVector, HashSet<MyVector>> actions(GameState state) {
        return state.getPossibleMoves();
    }//actions

    @Override
    public GameState result(GameState state, MyVector from, MyVector to) {
        byte currentPlayer = state.getPlayer();

        GameState newState = state.clone();
        newState.setPawn(from,to,currentPlayer);

        byte nextPlayer = currentPlayer == GameState.B ? GameState.W : GameState.B;
        newState.setPlayer(nextPlayer);
        return newState;
    }//result

    @Override
    public float utility(GameState state, byte to_move) {
        return 0;
    }

    @Override
    public int terminalTest(GameState state, byte player) {
        //The next player checks if state is terminal, so B & W win conditions are inverted
        boolean isKingCaptured = state.kingWasCaptured();
        boolean isKingEscaped = state.kingEscapes();

        if( isKingCaptured ){
            if( player==GameState.B ) return 100_000;
            else return -100_000;
        }
        if( isKingEscaped ){
            if( player==GameState.B ) return -100_000;
            else return 100_000;
        }

        return 0;
    }//terminalTest

    @Override
    public byte toMove(GameState state) {
        return state.getPlayer();
    }

    public static void main(String[] args) {
        //In order to get different move ordering each new match
        MyVector.initHash();

        byte[][] board = new byte[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = GameState.E;

        board[4][4] = GameState.K;

        board[2][4] = GameState.W;
        board[3][4] = GameState.W;
        board[5][4] = GameState.W;
        board[6][4] = GameState.W;
        board[4][2] = GameState.W;
        board[4][3] = GameState.W;
        board[4][5] = GameState.W;
        board[4][6] = GameState.W;

        board[0][3] = GameState.B;
        board[0][4] = GameState.B;
        board[0][5] = GameState.B;
        board[1][4] = GameState.B;
        board[8][3] = GameState.B;
        board[8][4] = GameState.B;
        board[8][5] = GameState.B;
        board[7][4] = GameState.B;
        board[3][0] = GameState.B;
        board[4][0] = GameState.B;
        board[5][0] = GameState.B;
        board[4][1] = GameState.B;
        board[3][8] = GameState.B;
        board[4][8] = GameState.B;
        board[5][8] = GameState.B;
        board[4][7] = GameState.B;

        GameState initState = new GameState(GameState.W, board);

        GameTablut tablut = new GameTablut( initState );

        int maxDepth = 6;
        Player p1 = new AlphaBetaCutoffPlayer(tablut, maxDepth, "white",60);
        //Player p2 = new QueryPlayer();
        Player p2 = new AlphaBetaCutoffPlayer(tablut, maxDepth, "black",60);
        tablut.play(p1,p2);
    }//main
}//GameTablut
