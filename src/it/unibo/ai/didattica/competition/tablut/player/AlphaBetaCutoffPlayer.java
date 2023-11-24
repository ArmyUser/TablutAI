package it.unibo.ai.didattica.competition.tablut.player;


import it.unibo.ai.didattica.competition.tablut.algorithms.AlphaBetaCutoffAlgorithm;
import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.game.Game;

public class AlphaBetaCutoffPlayer extends AIPlayer{
    public AlphaBetaCutoffPlayer(Game game, HistoryCommandHandler handler, int maxDepth) {
        super(new AlphaBetaCutoffAlgorithm(game,handler,maxDepth));
    }
}//AIPlayer
