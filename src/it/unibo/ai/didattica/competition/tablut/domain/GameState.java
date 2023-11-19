package it.unibo.ai.didattica.competition.tablut.domain;

import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Abstract class for a State of a game We have a representation of the board
 * and the turn
 */
public class GameState {
	private final char toMove;
	private final float utility;

	public GameState(char toMove, float utility){
		this.toMove = toMove;
		this.utility = utility;
	}

	public char getPlayer(){ return toMove; }

	public float getUtility(){ return utility; }
}//State
