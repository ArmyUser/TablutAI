package it.unibo.ai.didattica.competition.tablut.game;

import it.unibo.ai.didattica.competition.tablut.command.ActionCommand;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.player.AlphaBetaPlayer;
import it.unibo.ai.didattica.competition.tablut.player.Player;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;
import java.util.LinkedList;

public class GameTablut extends Game{

    int i = 0;

    public GameTablut(GameState initial){
        this.initial = initial;
        this.histCmdHandler = new HistoryCommandHandler();
    }

    @Override
    public HashMap<MyVector,LinkedList<MyVector>> actions(char player) {
        return BoardManager.getInstance().getPossibleMoves(player);
    }//actions

    @Override
    public GameState result(GameState state, MyVector from, MyVector to) {
        char currentPlayer = state.getPlayer();
        System.out.println("From: "+from);
        System.out.println("To: "+to);
        histCmdHandler.handle(new ActionCommand(from,to,currentPlayer));
        display();

        char nextPlayer = currentPlayer == BoardManager.B ? BoardManager.W : BoardManager.B;
        //System.out.println("Current: "+currentPlayer+"\nNext: "+nextPlayer);
        return new GameState(nextPlayer,computeUtility(currentPlayer), actions(nextPlayer));
    }//result

    @Override
    public float utility(GameState state, char to_move) {
        return 0;
    }

    private float computeUtility(char to_move){
        // winning conditions:
        // - WHITE: if the opponent have finished its pawns
        // - WHITE: if the King reaches jolly positions
        // - BLACK: if its pawns have captured the King

        if(to_move == BoardManager.B)
            if(BoardManager.getInstance().kingWasCaptured()) return -1;

        else if(BoardManager.getInstance().kingEscapes() || BoardManager.getInstance().allPawnsCaptured()) return 1;
        return 0;
    }//computeUtility

    @Override
    public boolean terminalTest(GameState state) {
        if( computeUtility(state.getPlayer()) != 0) {
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
            System.out.println("**********-----------------------------**********");
        }
        return computeUtility(state.getPlayer()) != 0;
    }

    @Override
    public char toMove(GameState state) {
        return state.getPlayer();
    }

    @Override
    public float eval(GameState state) {
        return 0;
    }

    public static void main(String[] args) {
        GameTablut tablut = new GameTablut(new GameState(BoardManager.W,0,BoardManager.getInstance().getPossibleMoves(BoardManager.W)));
        Player p1 = new AlphaBetaPlayer(tablut, tablut.histCmdHandler);
        Player p2 = new AlphaBetaPlayer(tablut, tablut.histCmdHandler);
        tablut.play(p1,p2);
    }//main
}//GameTablut
