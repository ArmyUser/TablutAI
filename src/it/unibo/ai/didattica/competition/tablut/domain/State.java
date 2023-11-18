package it.unibo.ai.didattica.competition.tablut.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Abstract class for a State of a game We have a representation of the board
 * and the turn
 */
public abstract class State {

	/**
	 * Turn represent the player that has to move or the end of the game(A win
	 * by a player or a draw)
	 */
	private final char WW = '1';
	private final char BW = '0';
	private final char D = 'D';

	/**
	 * Pawn represents the content of a box in the board
	 */
	private final char W = 'W';
	private final char B = 'W';
	private final char E = 'E';
	private final char T = 'T';
	private final char K = 'K';


	protected char board[][];
	protected char turn;

	public char[][] getBoard() {
		return board;
	}

	public String boardString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				result.append(board[i][j]);
				if (j == 8) result.append("\n");

			}
		}
		return result.toString();
	}//boardString

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		// board
		result.append("");
		result.append(boardString());

		result.append("-");
		result.append("\n");

		// TURN
		result.append(turn);

		return result.toString();
	}//toString

	public String toLinearString() {
		StringBuilder result = new StringBuilder();

		// board
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board.length; j++)
				result.append(board[i][j]);
		result.append(turn);

		return result.toString();
	}//toLinearString

	/**
	 * this function tells the pawn inside a specific box on the board
	 * 
	 * @param row
	 *            represents the row of the specific box
	 * @param column
	 *            represents the column of the specific box
	 * @return is the pawn of the box
	 */
	public char getPawn(int row, int column) {
		return board[row][column];
	}//getPawn

	/**
	 * this function remove a specified pawn from the board
	 * 
	 * @param row
	 *            represents the row of the specific box
	 * @param column
	 *            represents the column of the specific box
	 * 
	 */
	public void removePawn(int row, int column) {
		board[row][column] = E;
	}//removePawn

	public void setBoard(char[][] board) {
		this.board = board;
	}//setBoard

	public char getTurn() {
		return turn;
	}//getTurn

	public void setTurn(char turn) {
		this.turn = turn;
	}//setTurn

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof State))
			return false;
		State other = (State) obj;
		if (this.board == null) {
			if (other.board != null)
				return false;
		} else {
			if (other.board == null)
				return false;
			if (this.board.length != other.board.length)
				return false;
			for (int i = 0; i < other.board.length; i++)
				for (int j = 0; j < other.board[i].length; j++)
					if (this.board[i][j] != other.board[i][j])
						return false;
		}
		if (this.turn != other.turn)
			return false;
		return true;
	}//equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.board == null) ? 0 : deepHashCode(board));
		result = prime * result + Character.hashCode(this.turn);
		return result;
	}//hashCode
	
	private static int deepHashCode(char[][] matrix) {
		int tmp[] = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			tmp[i] = Arrays.hashCode(matrix[i]);
		}
		return Arrays.hashCode(tmp);
	}//deepHashCode

	public String getCellEncoding(int row, int column) {
		String ret;
		char col = (char) (column + 97);
		ret = col + "" + (row + 1);
		return ret;
	}//getCellEncoding

	public State clone() {

		Class<? extends State> stateclass = this.getClass();
		Constructor<? extends State> cons = null;
		State result = null;
		try {
			cons = stateclass.getConstructor(stateclass);
			result = cons.newInstance(new Object[0]);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		char oldboard[][] = this.getBoard();
		char newboard[][] = result.getBoard();

		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[i].length; j++) {
				newboard[i][j] = oldboard[i][j];
			}
		}

		result.setBoard(newboard);
		result.setTurn(this.turn);
		return result;
	}//clone

	/**
	 * Counts the number of checkers of a specific color on the board. Note: the king is not taken into account for white, it must be checked separately
	 * @param color The color of the checker that will be counted. It is possible also to use EMPTY to count empty cells.
	 * @return The number of cells of the board that contains a checker of that color.
	 */
	public int getNumberOf(char color) {
		int count = 0;
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				if (board[i][j] == color)
					count++;
		return count;
	}//getNumberOf
}//State
