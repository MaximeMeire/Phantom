package maximemeire.phantom.model.map;

import java.util.LinkedList;
import java.util.List;

import maximemeire.phantom.model.entity.player.Player;

public class Tile extends Location {
	
	protected List<Player> players = new LinkedList<Player>();
	
	public Tile(int x, int y, int z) {
		super(x, y, z);
	}
	
}
