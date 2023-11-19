package it.unibo.ai.didattica.competition.tablut.player;

import it.unibo.ai.didattica.competition.tablut.util.MyVector;

public interface Player {
    MyVector getNextAction(StateTablut state);
}//PlayerIF
