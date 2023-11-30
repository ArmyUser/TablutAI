package it.unibo.ai.didattica.competition.tablut.player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;

import com.google.gson.Gson;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameState;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.game.BoardManager;
import it.unibo.ai.didattica.competition.tablut.util.Configuration;
import it.unibo.ai.didattica.competition.tablut.util.StreamUtils;

/**
 * Classe astratta di un client per il gioco Tablut
 */
public abstract class TablutClient implements Runnable {

	private byte player;
	private String name;
	private Socket playerSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private Gson gson;
	private State currentState;
	private int timeout;
	private String serverIp;

	public byte getPlayer() {
		return player;
	}

	public void setPlayer(byte player) {
		this.player = player;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	/**
	 * Creates a new player initializing the sockets and the logger
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @param timeout
	 *            The timeout that will be taken into account (in seconds)
	 * @param ipAddress
	 *            The ipAddress of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		int port;
		serverIp = ipAddress;
		this.timeout = timeout;
		this.gson = new Gson();
		if (player.equalsIgnoreCase("white")) {
			this.player = BoardManager.W;
			port = Configuration.whitePort;
		} else if (player.equalsIgnoreCase("black")) {
			this.player = BoardManager.B;
			port = Configuration.blackPort;
		} else {
			throw new InvalidParameterException("Player role must be BLACK or WHITE");
		}
		playerSocket = new Socket(serverIp, port);
		out = new DataOutputStream(playerSocket.getOutputStream());
		in = new DataInputStream(playerSocket.getInputStream());
		this.name = name;
	}

	/**
	 * Creates a new player initializing the sockets and the logger. The server
	 * is supposed to be communicating on the same machine of this player.
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @param timeout
	 *            The timeout that will be taken into account (in seconds)
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name, int timeout) throws UnknownHostException, IOException {
		this(player, name, timeout, "localhost");
	}

	/**
	 * Creates a new player initializing the sockets and the logger. Timeout is
	 * set to be 60 seconds. The server is supposed to be communicating on the
	 * same machine of this player.
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name) throws UnknownHostException, IOException {
		this(player, name, 60, "localhost");
	}

	/**
	 * Creates a new player initializing the sockets and the logger. Timeout is
	 * set to be 60 seconds.
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @param ipAddress
	 *            The ipAddress of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name, String ipAddress) throws UnknownHostException, IOException {
		this(player, name, 60, ipAddress);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Write an action to the server
	 */
	public void write(Action action) throws IOException, ClassNotFoundException {
		StreamUtils.writeString(out, this.gson.toJson(action));
	}

	/**
	 * Write the name to the server
	 */
	public void declareName() throws IOException, ClassNotFoundException {
		StreamUtils.writeString(out, this.gson.toJson(this.name));
	}

	/**
	 * Read the state from the server
	 */
	public void read() throws ClassNotFoundException, IOException {
		currentState = this.gson.fromJson(StreamUtils.readString(in), StateTablut.class);
	}
}
