package it.unibo.ai.didattica.competition.tablut.player;


import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaAlgorithm;
import it.unibo.ai.didattica.competition.tablut.game.Game;

public class AlphaBetaPlayer extends AIPlayer {
    public AlphaBetaPlayer(Game game ){
        super(new AlphaBetaAlgorithm(game));
    }
}//AlphaBetaPlayer
