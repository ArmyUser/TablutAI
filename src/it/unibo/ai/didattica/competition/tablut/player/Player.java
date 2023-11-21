package it.unibo.ai.didattica.competition.tablut.player;

import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

public interface Player {
    MyVector[] getNextAction(GameState state);
}//PlayerIF
