package gamelogic;

import java.util.*;
import java.lang.*;
import catanui.*;


public class ClientGameBoard {

	private int _points_to_win;
	private ArrayList<Hex> _hexes;
	private ArrayList<Player> _players;
	private int _longestRd = 4;
	private int _longestRd_Owner = -1;
	client.Client _client;
	public int _playerNum;
	public catanui.ChatBar _chatBar;
	public catanui.SideBar _sideBar;
	public catanui.MapPanel _mapPanel;
	public String _name;
	private ArrayList<Trade> _currTrades;
	private Hashtable<CoordPair, Pair> _currVertexState;
	private Hashtable<Pair, Integer> _currEdgeState;
	private int _numPlayers;
	private HashMap<CoordPair, Integer> _coordMap;
	private ArrayList<Vertex> _vertices;
	private ArrayList<Pair> _ports;
	private int[] _points;
	private int[] _numRoads;
	
	public ClientGameBoard(int numPlayers, client.Client client, int playerNum, String name, String[] resources, ArrayList<Pair> ports, int points) {
		_points_to_win = points;
		_client = client;
		_hexes = new ArrayList<Hex>();
		_players = new ArrayList<Player>();
		_playerNum = playerNum;
		_name = name;
		_currVertexState = new Hashtable<CoordPair, Pair>();
		_currEdgeState = new Hashtable<Pair, Integer>();
		_numPlayers = numPlayers;
		_coordMap = new HashMap<CoordPair, Integer>();
		_vertices = new ArrayList<Vertex>();
		_ports = ports;
		_points = new int[numPlayers];
		_numRoads = new int[numPlayers];
		
		for (int i = 0; i<numPlayers; i++) {
		    _points[i] = 0;
		    _numRoads[i] = 0;
		}
		
		setUpBoard(numPlayers, resources);
	}
	
	//set up hexes and vertices
	public void setUpBoard(int numPlayers, String[] resources) {
	    ArrayList<Integer> colSizes = null;
	    ArrayList<Integer> startY = null;
	    ArrayList<Integer> numbers = null;
	    int numHexes = 0;
	    if (numPlayers <= 4) {
			colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 4, 3));
			startY = new ArrayList<Integer>(Arrays.asList(3, 2, 1, 2, 3));
			numHexes = 19;
			numbers = new ArrayList<Integer>(Arrays.asList(8,4,11,10,11,3,12,5,9,2,6,9,2,4,5,10,6,3,8));
	    } else if (numPlayers == 5 || numPlayers == 6) {
			colSizes = new ArrayList<Integer>(Arrays.asList(4,5,6,7,6,5,4));
			startY = new ArrayList<Integer>(Arrays.asList(4,3,2,1,2,3,4));
			numHexes = 37;
			numbers = new ArrayList<Integer>(Arrays.asList(9,8,11,11,3,4,10,5,10,6,12,3,2,4,6,4,11,12,6,9,3,5,5,10,9,5,8,9,2,3,8,4,2,6,10,12,8));
	    }
	    
	    double currx = -0.5;
	    double curry;
	    int hexCount = 0;
	    for (int i=0; i<colSizes.size(); i++) {
			currx += 2;
			curry = startY.get(i);
			for (int x=0; x<colSizes.get(i); x++) { //create hexes
				Hex hex = new Hex(hexCount, currx, curry);
				hex.setResource(catanui.BoardObject.type.valueOf(resources[hexCount]));
				hex.setRollNum(numbers.get(hexCount));
				_hexes.add(hex);
				hexCount++;
				
				//create vertices
				ArrayList<Vertex> vertices = new ArrayList<Vertex>();
				if(_vertices.contains(new Vertex((int)(currx-1.5), (int)(curry)))) {
					vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx-1.5), (int)(curry)))));
				} else {
					vertices.add(new Vertex((int)(currx-1.5), (int)(curry)));
				} if(_vertices.contains(new Vertex((int)(currx-.5), (int)(curry-1)))) {
					vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx-.5), (int)(curry-1)))));
				} else {
					vertices.add(new Vertex((int)(currx-.5), (int)(curry-1)));
				} if(_vertices.contains(new Vertex((int)(currx+.5), (int)(curry-1)))) {
					vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx+.5), (int)(curry-1)))));
				} else {
					vertices.add(new Vertex((int)(currx+.5), (int)(curry-1)));
				} if(_vertices.contains(new Vertex((int)(currx+1.5), (int)(curry)))) {
					vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx+1.5), (int)(curry)))));
				} else {
					vertices.add(new Vertex((int)(currx+1.5), (int)(curry)));
				} if(_vertices.contains(new Vertex((int)(currx+.5), (int)(curry+1)))) {
					vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx+.5), (int)(curry+1)))));
				} else {
					vertices.add(new Vertex((int)(currx+.5), (int)(curry+1)));
				} if(_vertices.contains(new Vertex((int)(currx-.5), (int)(curry+1)))) {
					vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx-.5), (int)(curry+1)))));
				} else {
					vertices.add(new Vertex((int)(currx-.5), (int)(curry+1)));
				}
				hex.setVertices(vertices);
				
				for (int z=0; z<(vertices.size()); z++) {
					if (!_vertices.contains(vertices.get(z))) {
						_vertices.add(vertices.get(z));
						_coordMap.put(new CoordPair(vertices.get(z).getX(), vertices.get(z).getY()), 
									new Integer(_vertices.indexOf(vertices.get(z))));
					}
				}
				curry += 2;
			}
	    }
	}
	
	//update the trades section of the GUI
	public void updateGUI(Trade t, boolean b) {
		if(t.isPropose()) {
			_sideBar.signalNewTrade(t);
	    } else {
			if(t.getTradeID() == -1) {
				catanui.BoardObject.type[] ar = t.getOuts();
				for(int i=0; i<ar.length; i++) {
					if(ar[i] != null) {
						_sideBar.addCard(ar[i]);
					}
				}
			} else {
				_sideBar.activateExchanger(t.getTradeID(), b);
			}
			
	    }
	}

	//send request to buy a settlement
	public void writeBuySettlement(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 6);
	    _client.sendRequest(t);
	}

	//send request to build a settlement at a specific location
	public void writeBuildSettlement(int vx, int vy) {
		_client.sendRequest(2, Integer.toString(_playerNum) + "," + 
		Integer.toString(vx) + "," + Integer.toString(vy));
	}
	
	public void buildSettlement(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    _vertices.get(v).setOwner(p);
	    _vertices.get(v).setObject(1);
	    _points[p]++;
	    if (_points[p] >= _points_to_win && p == _playerNum) {
			sendWin(_name);
	    }
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.SETTLEMENT, p));
	    _mapPanel.updateVertexContents(_currVertexState);
	}
	
	//send request to buy a road
	public void writeBuyRoad(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 5);
	    _client.sendRequest(t);
	}

	//send request to build a road at a specific location
	public void writeBuildRoad(int vx1, int vy1, int vx2, int vy2) {
		_client.sendRequest(1, Integer.toString(_playerNum) + "," + Integer.toString(vx1) + "," + Integer.toString(vy1) + "," + Integer.toString(vx2) + "," + Integer.toString(vy2));
	}
	
	public void buildRoad(int p, int vx1, int vy1, int vx2, int vy2) {
	    _numRoads[p]++;
	    updateLongestRoad(p);
	    _currEdgeState.put(new Pair(new CoordPair(vx1, vy1), new CoordPair(vx2, vy2)), new Integer(p));
	    _mapPanel.updateEdgeContents(_currEdgeState);
	}
	
	//send request to buy a city
	public void writeBuyCity(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 4);
	    _client.sendRequest(t);
	}
	
	//send request to build a city at a specific location
	public void writeBuildCity(int vx, int vy) {
		_client.sendRequest(3, Integer.toString(_playerNum) + "," + 
			Integer.toString(vx) + "," + Integer.toString(vy));
	}
	
	public void buildCity(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    _vertices.get(v).setOwner(p);
	    _vertices.get(v).setObject(2);
	    _points[p]++;
	    if (_points[p] >= _points_to_win && p == _playerNum) {
		sendWin(_name);
	    }
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.CITY, p));
	    _mapPanel.updateVertexContents(_currVertexState);
	}
	
	//send request to buy a development card
	public void writeBuyDev(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 3);
	    _client.sendRequest(t);
	}
	
	public void useDevCard() {
		_client.sendRequest(17, "usedev");
		sendMessage("9" + _name + " has played a Development Card.");
	}
	
	//send request to propose a trade
	public void writeProposeTrade(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) { 
		Trade t = new Trade(ins, outs, id, 2);
		_client.sendRequest(t);
	}
	
	//send request to complete a specific trade with a specific player
	public void writeDoTrade(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 1);
		_client.sendRequest(t);
	}
	
	//send request to use a port to complete a trade
	public void exchangePort(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 7);
		_client.sendRequest(t);
	}
	
	//send request to remove a specific trade from the trade sidebar
	public void writeRemoveTrade(int id) {
		_client.sendRequest(23, "" + id);
	}
	
	//remove a specific trade from the trade side bar
	public void removeTrade(int id) {
		_sideBar.removeTrade(id);
	}
	
	public void diceRolled(int roll) {
	    _mapPanel.updateRoll(roll);
	    for (Hex h : _hexes) {
			if (h.getRollNum() == roll) {
				for (Vertex vertex : h.getVertices()) {
					int p = vertex.getOwner();
					if (p == _playerNum) {
						_sideBar.addCard(h.getResource());
						if (vertex.getObject() == 2)  { //if city
							_sideBar.addCard(h.getResource());
							_chatBar.addLine("9You received two " + h.getResource());
							sendMessage("9" + _name + " received two " + h.getResource());
						}else {
							_chatBar.addLine("9You received one " + h.getResource());
							sendMessage("9" + _name + " received one " + h.getResource());
						}
					}
				}
			}
	    }
	}
	
	//update which player currently has the most road
	public void updateLongestRoad(int p) {
	    if (_numRoads[p] > _longestRd) {
			if (_longestRd_Owner != -1) {
				_points[_longestRd_Owner] -= 2;
			}
			_points[p] += 2;
			_longestRd = _numRoads[p];
			if (p == _playerNum && p != _longestRd_Owner) {
				_chatBar.addLine("9" + "You have the Largest Road Network! You now have " + _points[_playerNum] + " points.");
				sendMessage("9" + _name + " now has the Largest Road Network!");
			}
			_longestRd_Owner = p;
			if (_points[p] >= _points_to_win && p == _playerNum) {
				sendWin(_name);
			}
	    }
	}
	
	//this player gets a point from a development card
	public void addPoint() {
	    _points[_playerNum]++;
	    _chatBar.addLine("9" + "You have received a point! You now have " + _points[_playerNum] + " points.");
	    if (_points[_playerNum] >= _points_to_win) {
		    sendWin(_name);
		}
	}
	
	//receives a single card from a development card
	public void addCard(BoardObject.type card) {
	    _sideBar.addCard(card);
	    _chatBar.addLine("9" + "You received a " + card);
	}
	
	//lose cards another player stole from you using a monopoly development card
	public void loseStolenCards(BoardObject.type card) {
	    _sideBar.removeAllCards(card);
	    _chatBar.addLine("9" + "All of your " + card + " were stolen!");
	}
	
	//get the cards you stole from the other players using a monopoly development card
	public void getStolenCards(int num, BoardObject.type card) {
	    for (int i=0; i<num; i++) {
		_sideBar.addCard(card);
	    }
	    _chatBar.addLine("9" + "You stole " + num + " " + card + " from the other players.");
	}
	
	//get two free roads from a development card
	public void freeRoads() {
	    _chatBar.addLine("9" + "You have received 2 free roads to build!");
	}
	
	//send a chat bar message to all the players
	public void sendLine(String s) {
	    _client.sendRequest(10, s);
	}
	
	//send a private chat bar message to a specific player
	public void sendLinePrivate(String message, String name) {
	    _client.sendRequest(4, name + "," + message);
	}
	
	//send a system message to be displayed in all players' chat bars
	public void sendMessage(String s) {
	    _client.sendRequest(11, s);
	}
	
	//you think you won
	public void sendWin(String s) {
	    _client.sendRequest(9, s);
	}
	
	//receive a line to be placed in you chat bar
	public void receiveLine(String s) {
	    _chatBar.addLine(s);
	}
	
	//tell the GUI that the game is over and who won
	public void gameOver(String name) {
	    _mapPanel.gameOver(name);
	}
	
	//when you build a settlement on a port, add the port trade to you port side bar
	public void addPort(BoardObject.type type) {
	    _sideBar.addPort(type);
	}
	
	//set up the ports for the GUI
	public void setPorts() {
	    Hashtable<Pair, BoardObject.type> toSend = new Hashtable<Pair, BoardObject.type>();
	    for (int i=0; i<(_ports.size()-1); i+=2) {
		toSend.put(new Pair(_ports.get(i).getA(), _ports.get(i+1).getA()), (BoardObject.type)(_ports.get(i).getB()));
	    }
	    _mapPanel.updatePortContents(toSend);
	}
	
	//GUI wants the hex info
	public HashMap<Pair, Pair> getHexInfo() {
	    HashMap<Pair, Pair> map = new HashMap<Pair, Pair>();
	    for (Hex h: _hexes) {
			map.put(new Pair(h.getX(), h.getY()), new Pair(h.getResource(), h.getRollNum()));
	    }
	    setPorts();
	    return map;
	}
	
	//GUI wants the number of rings on the board: 3 if <= 4 players and 4 if > 4 players
	public int getNumRings() {
		if (_numPlayers <= 4) {
			return 3;
		} else {
			return 4;
		}
	}
	
	//GUI wants the coordinates of the center hex of the board
	public Pair getStartPoint() {
	    if (_numPlayers <= 4) {
		Pair start = new Pair(_hexes.get(9).getX(), _hexes.get(9).getY());
		return start;
	    } else {
		Pair start2 = new Pair(_hexes.get(18).getX(), _hexes.get(18).getY());
		return start2;
	    }
	}
	public void exit() {
	}
	
}
