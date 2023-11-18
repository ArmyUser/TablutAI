package it.unibo.ai.didattica.competition.tablut.domain;

import java.io.Serial;
import java.io.Serializable;


/**
 * This class represents a state of a match of Tablut (classical or second
 * version)
 */
public class StateTablut extends State implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public StateTablut() {
		super();
		board = new char[9][9];

		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				board[i][j] = E;

		this.board[4][4] = T;

		this.turn = B;

		this.board[4][4] = K;

		this.board[2][4] = W;
		this.board[3][4] = W;
		this.board[5][4] = W;
		this.board[6][4] = W;
		this.board[4][2] = W;
		this.board[4][3] = W;
		this.board[4][5] = W;
		this.board[4][6] = W;

		this.board[0][3] = B;
		this.board[0][4] = B;
		this.board[0][5] = B;
		this.board[1][4] = B;
		this.board[8][3] = B;
		this.board[8][4] = B;
		this.board[8][5] = B;
		this.board[7][4] = B;
		this.board[3][0] = B;
		this.board[4][0] = B;
		this.board[5][0] = B;
		this.board[4][1] = B;
		this.board[3][8] = B;
		this.board[4][8] = B;
		this.board[5][8] = B;
		this.board[4][7] = B;
	}//Constructor

	public StateTablut clone() {
		StateTablut result = new StateTablut();

		char oldboard[][] = this.getBoard();
		char newboard[][] = result.getBoard();

		for (int i = 0; i < this.board.length; i++)
			for (int j = 0; j < this.board[i].length; j++)
				newboard[i][j] = oldboard[i][j];

		result.setBoard(newboard);
		result.setTurn(this.turn);
		return result;
	}//clone
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StateTablut))
			return false;
		StateTablut other = (StateTablut) obj;
		if (this.board == null) {
			if (other.board != null)
				return false;
		} else {
			if (other.board == null)
				return false;
			if (this.board.length != other.board.length)
				return false;
			if (this.board[0].length != other.board[0].length)
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
}//StateTablut
