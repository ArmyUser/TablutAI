package it.unibo.ai.didattica.competition.tablut.algorithms;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

public abstract class AbstractAlgorithms {
    protected final Game game;
    protected byte player;
    protected final HistoryCommandHandler handler;


    public AbstractAlgorithms(Game game, HistoryCommandHandler handler){
        this.game = game;
        this.handler = handler;
    }

    public abstract MyVector[] searchForBestAction(GameState state);
}//AbstractAlgorithms
