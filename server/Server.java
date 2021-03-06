package server;

import java.io.*;
import java.net.*;
import java.util.*;
import catanui.*;

/**
 * A chat server, listening for incoming connections and passing them
 * off to {@link ClientHandler}s.
 */
public class Server extends Thread {
	private final int SECONDS_PER_TURN = 5;
	private int _port;
	private ServerSocket _socket;
	private ClientPool _clients;
	private boolean _running;
	private int _numClients;
	private gamelogic.PublicGameBoard _board;
	private boolean _keepListening;
	private int _numConnections;
	private int _numAI;
	private catanui.SplashScreen _splash;
	private int _rollInterval;

	public ClientPool getClientPool() {
		return _clients;
	}
	


	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int port, int numCon, int numAI, catanui.SplashScreen introScreen, int roll) throws IOException {
		if (port <= 1024) {
			throw new IllegalArgumentException("Ports under 1024 are reserved!");
		}
		_port = port;
		_clients = new ClientPool(numCon + numAI, this);
		_socket = new ServerSocket(_port);
		_keepListening = true;
		_running = true;
		_numClients = 0;
		_numConnections = numCon;
		_numAI = numAI;
		_splash = introScreen;
		_rollInterval = roll;
		
		_board = new gamelogic.PublicGameBoard(this, numCon + numAI);
		_clients.addBoard(_board);
		/*Add the AI players.*/
		for (int i = numCon; i < numCon + numAI; i++) {
			catanai.AIPlayer ai = new catanai.AIPlayer(_board, Integer.toString(i), this, numCon + numAI >= 5);
			for (int j = 0; j < numCon + numAI; j++) {
				if (i == j) continue;
				ai.addOpponent(Integer.toString(j));
			}
			_board.addAIPlayer(ai, i);
		}
	}

	/**
	 * Wait for and handle connections indefinitely.
	 */
	public void run() {
		try {
			while(_numClients < _numConnections) {
				Socket clientConnection = _socket.accept();
				if(_numClients < _numConnections) {
					ClientHandler ch = new ClientHandler(_clients, clientConnection, _numClients);
					_clients.add(ch, _numClients);
					_clients.initMessage(ch);
					ch.start();
					_numClients++;
				} else {
					System.out.println("no socket");
					clientConnection.close();
				}
			}
			_socket.close();
			stopListening();
			
			_splash.enterLoop();
		} catch(IOException e) {
		
		}
	}
	
	/**
	 * Stop waiting for connections, close all connected clients, and close
	 * this server's {@link ServerSocket}.
	 * 
	 * @throws IOException if any socket is invalid.
	 */
	public void kill()  {
		try {
			_running = false;
			_clients.killall();
			_socket.close();
		} catch (Exception e) {
			
		}
	}
	
	public boolean getRunning() {
		return _running;
	}
	
	public boolean everyonesReady() {
		return !_keepListening;
	}
	
	public void stopListening() {
		_keepListening = false;
		try {
			sleep((long) Math.random() * 100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_board.promptInitRoundAI();
	}
	
	// Registers a roll with the board and broadcasts it
	public void roll(int r) {
		_board.diceRolled(r);
		_clients.broadcast("1/" + (r), null);
	}
	
	// Begins generating a random roll every _rollInterval seconds
	public void beginTimer() {
		_clients.broadcast("10/9Welcome to HexCraft", null);
		String AIstring = "";
		if(_numAI == 1) {
			AIstring = " & " + _numAI + " AI player";
		} else if(_numAI > 1) {
			AIstring = " & " + _numAI + " AI players";
		}
		_clients.broadcast("10/9The players are: " + _clients.getNames() + AIstring, null);
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(Server.this._running) {
					int roll1 = (int) ((Math.random() * 6) + 1);
					int roll2 = (int) ((Math.random() * 6) + 1);
					while(roll1 + roll2 == 7) {
						roll1 = (int) ((Math.random() * 6) + 1);
						roll2 = (int) ((Math.random() * 6) + 1);
					}
					Server.this._board.diceRolled(roll1 + roll2);
					Server.this._clients.broadcast("1/" + (roll1+roll2), null);
				}
			}
		}, 0, _rollInterval * 1000);

	}
	
	// Sends free cards to the specified player
	public void sendFreeCards(int p, catanui.BoardObject.type[] ar) {
		catanui.BoardObject.type[] ar2 = new catanui.BoardObject.type[0];
		gamelogic.Trade t = new gamelogic.Trade(ar2, ar, -1, 1);
		_clients.broadcastTo(t, p);

	}
	
	// Sends information about a port to the player
	public void sendPort(int p, BoardObject.type type) {
	    _clients.broadcastTo("33/" + type.toString(), p);
	}
	
	public void sendWin(int p) {
		int aiIndex = p-_numConnections;
		String message = "9/AI Player " + aiIndex;
		_clients.broadcast("10/9AI Player " + aiIndex + " has won.", null);
		_clients.broadcast(message, null);
		_clients.killall();
	}
}

