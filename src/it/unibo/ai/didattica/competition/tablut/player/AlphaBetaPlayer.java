package it.unibo.ai.didattica.competition.tablut.player;


import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaAlgorithm;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.game.Game;

public class AlphaBetaPlayer extends AIPlayer {
    public AlphaBetaPlayer(Game game, HistoryCommandHandler handler){
        super(new AlphaBetaAlgorithm(game,handler));
    }
}//AlphaBetaPlayer
