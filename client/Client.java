package client;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;
import gamelogic.*;
import catanui.*;

/** 
 * A chat client
 */
public class Client extends Thread {
	private int _port;
	private String _host;
	private Socket _socket;
	private ObjectInputStream _objectIn;
	private ObjectOutputStream _objectOut;
	private LinkedBlockingQueue<Request> _requests;
	private boolean _continue;
	private gamelogic.ClientGameBoard _board;
	

	public Client(int port, String host, String name, catanui.SplashScreen splashScreen) throws IOException{
		_requests = new LinkedBlockingQueue<Request>(20);

			if (port <= 1024) {
				throw new IllegalArgumentException("Ports under 1024 are reserved!");
			}
			try {
			_port = port;
			_host = host;
			Socket _socket = new Socket();
			InetSocketAddress i = new InetSocketAddress(_host, _port);
			_socket.connect(i, 1000);
			//_socket = new Socket(_host, _port);
			_objectIn = new ObjectInputStream(_socket.getInputStream());
			_objectOut = new ObjectOutputStream(_socket.getOutputStream());
			_continue = true;
			
			
				String id = (String) _objectIn.readObject();
				String[] split = id.split(",");
				
				String gameState = (String) _objectIn.readObject();
				String[] resources = gameState.split(",");
				
				splashScreen.close();
				// TODO: Change later
				_board = new gamelogic.ClientGameBoard(Integer.parseInt(split[1]), this, Integer.parseInt(split[0]), name, resources);
				catanui.Board b = new catanui.Board(_board);
			} catch(SocketTimeoutException e) {
				splashScreen.beginHome();
				System.out.println("Connection failed");
			} catch (IOException e) {
				splashScreen.beginHome();
				System.out.println("Connection failed");
			} catch(Exception e) {
				e.printStackTrace();
			}
	}

	public void run() {
		InputReader in = new InputReader(_objectOut, _requests, this);
		in.start();
		int opcode;
		String details[];

		try { 
			while(_continue) {
				// read object should block
				Object o = _objectIn.readObject();
				if(o.getClass().equals(String.class)) {
					String[] line = ((String) o).split("/");
					try {
						opcode = Integer.parseInt(line[0]);
						details = line[1].split(",");
						
						switch(opcode) {
							case 1:
								_board.diceRolled(Integer.parseInt(details[0]));
								break;
							case 3:
								_board.buildRoad(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]), 
													Integer.parseInt(details[3]), Integer.parseInt(details[4]));
								break;
							case 4:
								_board.buildSettlement(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]));
								break;
							case 5:
								_board.buildCity(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]));
								break;
							case 7:
								Trade t = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK}, new BoardObject.type[] {BoardObject.type.ROAD}, 1, 5);
								Trade t2 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK, BoardObject.type.WHEAT, BoardObject.type.SHEEP}, new BoardObject.type[] {BoardObject.type.SETTLEMENT}, 0, 6);
								_board.updateGUI(t, true);
								_board.updateGUI(t2, true);
								_board.updateGUI(t, true);
								_board.updateGUI(t2, true);
								break;
							case 10:
								String toDisplay = "";
								
								for(int i=1; i<line.length; i++) {
									if(i+1 < line.length) {
										toDisplay += line[i] + "/";
									} else {
										toDisplay += line[i];
									}
								}
								_board.receiveLine(toDisplay);
								break;
							case 11:
								Trade t3 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK}, new BoardObject.type[] {BoardObject.type.ROAD}, 1, 5);
								_board.updateGUI(t3, true);
								break;
							case 12:
								Trade t4 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK, BoardObject.type.WHEAT, BoardObject.type.SHEEP}, new BoardObject.type[] {BoardObject.type.SETTLEMENT}, 0, 6);
								_board.updateGUI(t4, true);
								break;
							case 13:
								Trade t5 = new Trade(new BoardObject.type[] { BoardObject.type.ORE,  BoardObject.type.WHEAT, BoardObject.type.WHEAT, BoardObject.type.ORE, BoardObject.type.ORE}, new BoardObject.type[] {BoardObject.type.CITY}, 3, 4);
								_board.updateGUI(t5, true);
								break;
								
							case 21:
								_board.addPoint();
								break;
							
							case 22:
							    Trade t6 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK}, new BoardObject.type[] {BoardObject.type.ROAD}, 1, 5);
							    _board.updateGUI(t6, true);
							    _board.updateGUI(t6, true);
							    _board.freeRoads();
							    break;
							    
							case 23:
							    BoardObject.type card = null;
							    for (BoardObject.type c: BoardObject.cardtypes) {
								if (details[0].equalsIgnoreCase(c.toString())) {
								    card = c;
								}
							    }
							    _board.addCard(card);
							    break;
							    
							case 24:
							    int num = Integer.parseInt(details[0]);
							    BoardObject.type type2 = null;
							    for (BoardObject.type c3: BoardObject.cardtypes) {
								if (details[1].equalsIgnoreCase(c3.toString())) {
								    type2 = c3;
								}
							    }
							    _board.getStolenCards(num, type2);
							    break;
							    
							case 25:
							    BoardObject.type type = null;
							    for (BoardObject.type c2: BoardObject.cardtypes) {
								if (details[0].equalsIgnoreCase(c2.toString())) {
								    type = c2;
								}
							    }
							    _board.loseStolenCards(type);
							    break;
							default:
								break;
						}
					} catch (NumberFormatException e) {
						opcode = 0;
					} catch (ArrayIndexOutOfBoundsException e) {
						opcode = 0;
						details = new String[1];
						details[0] = "exit";
					}
				} else {
					Trade ex = (Trade) o;
					// TODO: Fix here
					_board.updateGUI(ex, false);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() == null ? "Cannot connect to server" : e.getMessage());
			
		} finally {
			try {
				_objectIn.close();
			} catch (Exception e) {}
			try {
				_objectOut.close();
			} catch (Exception e) {}
			try {
				_socket.close();
			} catch (Exception e) {}
		}
	}
	
	public void sendRequest(Object e) {
		Request r = new Request(e);
		_requests.offer(r);
	}
	
	public void sendRequest(int i, String s) {
		Request r = new Request(i + "/" + s);
		_requests.offer(r);
	}
	
	
	public void stopListening() {
		_continue = false;
	}
	
	public boolean getContinue() {
		return _continue;
	}	
	
	private class InputReader extends Thread {
		private ObjectOutputStream _objectOut;
		private LinkedBlockingQueue<Request> _requests;
		private Client _client;
		
		private boolean _continue;
		
		public InputReader(ObjectOutputStream out, LinkedBlockingQueue<Request> requests, Client c) {
			_objectOut = out;
			_requests = requests;
			_client = c;
			_continue = false;
		}
		
		public void run() {
			try { 
				while(_client.getContinue()) {
					if(_requests.peek() != null) {
						Request r = _requests.poll();
						Object r1 = r.getRequest();
						if (r1.getClass().equals(Trade.class)) {
							((Trade) r1).backup();
							_objectOut.writeObject(r1);
							_objectOut.flush();
						}
						else {						
							_objectOut.writeObject(r.getRequest());
							_objectOut.flush();
						}
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage() == null ? "Cannot connect to server" : e.getMessage());
			}
		}
	}
}
