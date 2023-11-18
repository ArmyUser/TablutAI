package it.unibo.ai.didattica.competition.tablut.domain;

import it.unibo.ai.didattica.competition.tablut.util.MyVector;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * this class represents an action of a player
 */
public class Action implements Serializable {

	private static final long serialVersionUID = 1L;

	private MyVector from;
	private MyVector to;

	private char turn;

	public Action(int xFrom, int yFrom, int xTo, int yTo, char turn) throws IOException {
		if (xFrom < 0 || xFrom > 8 || xTo < 0 || xTo > 8 ||
				yFrom < 0 || yFrom > 8 || yTo < 0 || yTo > 8 || (xFrom==xTo && yFrom == yTo) ){
			throw new InvalidParameterException("Invalid action");
		} else {
			from = new MyVector(xFrom, yFrom);
			to = new MyVector(xTo, yTo);
			this.turn = turn;
		}
	}

	public MyVector getFrom() {
		return from;
	} //DEEP?

	public void setFrom(MyVector from) {
		this.from = new MyVector(from.x, from.y);
	}//setFrom

	public MyVector getTo() {
		return to;
	} //DEEP?

	public void setTo(MyVector to) {
		this.to = new MyVector(to.x, to.y);
	}//setTo

	public char getTurn() {
		return turn;
	}

	public void setTurn(char turn) {
		this.turn = turn;
	}

	public String toString() {
		return "Turn: " + turn + " " + "Pawn from " + from + " to " + to;
	}

	/**
	 * @return means the index of the column where the pawn is moved from
	 */
	public int getColumnFrom() {
		return from.y;
	}

	/**
	 * @return means the index of the column where the pawn is moved to
	 */
	public int getColumnTo() {
		return to.y;
	}

	/**
	 * @return means the index of the row where the pawn is moved from
	 */
	public int getRowFrom() {
		return from.x;
	}

	/**
	 * @return means the index of the row where the pawn is moved to
	 */
	public int getRowTo() {
		return to.x;
	}
}//Action
