package catanai;

import java.util.List;
import server.Server;
import gamelogic.PublicGameBoard;
import gamelogic.Trade;
import catanui.BoardObject;

public class ProposeTrade extends Move implements AIConstants {
	private int _id;
	private List<Resource> _to, _from;
	private gamelogic.Trade _tb;
	private Server _s;
	
	public ProposeTrade(Player p, List<Resource> t, List<Resource> f, Server s) {
		_mover = p;
		_to = t;
		_from = f;
		_s = s;
		_id = -1;
		_tb = null;
	}
	
	public ProposeTrade(Player p, List<Resource> t, List<Resource> f, int id) {
		_mover = p;
		_to = t;
		_from = f;
		_s = null;
		
		BoardObject.type to[] = new BoardObject.type[t.size()];
		BoardObject.type from[] = new BoardObject.type[f.size()];
		int i = 0;
		for (Resource r : t) {
			to[i] = RES_CONV.get(r);
			i++;
		}
		i = 0;
		for (Resource r : f) {
			from[i] = RES_CONV.get(r);
			i++;
		}
		_id = id;
		_tb = new Trade(to, from, _id, 2);
	}
	
	@Override
	public void broadcast(AIPlayer p, PublicGameBoard board) {
		p.addPendingTrade(this);
		p.broadcast(_tb);
	}

	@Override
	public void charge() {
		_mover.dropList(_to);
	}

	@Override
	public boolean make(PublicGameBoard board) {
		if (_mover.hasList(_to)) {
			if (! isSigned()) sign();
			if (isSigned()) return true;
		}
		return false;
	}

	@Override
	public boolean place(GameBoard board) {
		return _mover.hasList(_to);
	}
	
	public int getID() {
		return _id;
	}
	
	public List<Resource> getTo() {
		return _to;
	}
	
	public List<Resource> getFrom() {
		return _from;
	}
	
	public FulfillTrade fulfill(Player p) {
		return new FulfillTrade(_mover, p, _to, _from, _id);
	}
	
	private void sign() {
		if (_s != null) {
			_id = _s.getClientPool().nextTradeID(Integer.parseInt(_mover.getID()));
			BoardObject.type to[] = new BoardObject.type[_to.size()];
			BoardObject.type from[] = new BoardObject.type[_from.size()];
			int i = 0;
			for (Resource r : _to) {
				to[i] = RES_CONV.get(r);
				i++;
			}
			i = 0;
			for (Resource r : _from) {
				from[i] = RES_CONV.get(r);
				i++;
			}
			_tb = new Trade(to, from, _id, 2);
		}
	}
	
	public boolean isSigned() {
		return _id != -1;
	}
	
	@Override
	public String toString() {
		return _to.toString() + " --> " + _from.toString();
	}
}
