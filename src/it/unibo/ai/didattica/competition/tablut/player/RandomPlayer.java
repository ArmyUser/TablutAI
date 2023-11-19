package it.unibo.ai.didattica.competition.tablut.player;

import it.unibo.ai.didattica.competition.tablut.game.Game;
import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.util.LinkedList;
import java.util.Random;

public class RandomPlayer extends HumanPlayer {
    private Game game;
    private Random rand;

    public RandomPlayer(Game game){
        this.game = game;
        rand = new Random();
    }

    @Override
    public MyVector getNextAction(StateTablut state) {
        LinkedList<MyVector> actions = game.actions(state);
        if( !actions.isEmpty() ){
            return actions.get(rand.nextInt(actions.size()));
        }
        return null;
    }//getNextAction
}//RandomPlayer
