package it.unibo.ai.didattica.competition.tablut.player;

import it.unibo.ai.didattica.competition.tablut.algorithms.AbstractAlgorithms;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.io.IOException;
import java.net.UnknownHostException;

public abstract class OnlinePlayer extends TablutClient implements Player{
    protected AbstractAlgorithms algorithm;

    protected OnlinePlayer(AbstractAlgorithms algorithm, String player, String name, int timeout, String ipAddress) throws IOException {
        super(player, name, timeout, ipAddress);
        this.algorithm = algorithm;
    }

    @Override
    public MyVector[] getNextAction(GameState state){
        long startingTime = System.currentTimeMillis();
        MyVector[] move = algorithm.searchForBestAction(state);
        System.out.println("Time spent for the move: "+(System.currentTimeMillis()-startingTime)/1000+"s");
        return move;
    }//getNextAction
}//OnlinePlayer
