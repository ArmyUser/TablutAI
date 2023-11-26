package it.unibo.ai.didattica.competition.tablut.domain;

import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Abstract class for a State of a game We have a representation of the board
 * and the turn
 */
public class GameState {
	private final char toMove;
	private final HashMap<MyVector, HashSet<MyVector>> moves;

	public GameState(char toMove, HashMap<MyVector,HashSet<MyVector>> moves){
		this.toMove = toMove;
		this.moves = moves;
	}

	public char getPlayer(){ return toMove; }

	public HashMap<MyVector,HashSet<MyVector>> getMoves(){ return moves; }
}//State
