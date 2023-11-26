package it.unibo.ai.didattica.competition.tablut.algorithms;

import it.unibo.ai.didattica.competition.tablut.command.HistoryCommandHandler;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.HashMap;

public abstract class AbstractAlgorithms {
    protected final Game game;
    protected char player;
    protected final HistoryCommandHandler handler;
    protected HashMap<Integer, Float> traspositionTable;


    public AbstractAlgorithms(Game game, HistoryCommandHandler handler){
        this.game = game;
        this.handler = handler;
        this.traspositionTable = new HashMap<>();
    }

    public abstract MyVector[] searchForBestAction(GameState state);
}//AbstractAlgorithms
