package it.unibo.ai.didattica.competition.tablut.player;

import is.algorithms.AlphaBetaAlgorithm;
import is.command.HistoryCommandHandler;
import is.game.Game;

public class AlphaBetaPlayer extends AIPlayer {
    public AlphaBetaPlayer(Game game, HistoryCommandHandler handler){
        super(new AlphaBetaAlgorithm(game,handler));
    }
}//AlphaBetaPlayer
