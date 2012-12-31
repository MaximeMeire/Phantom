package maximemeire.phantom.testing;

import java.util.LinkedList;
import java.util.List;

import maximemeire.phantom.concurrent.ActorUniverse;
import maximemeire.phantom.concurrent.impl.actors.player.Player;
import maximemeire.phantom.concurrent.impl.actors.player.PlayerMoveMessage;
import maximemeire.phantom.concurrent.impl.actors.player.StandardPlayerHandler;

/**
 * Testing purpose.
 * @author Maxime Meire
 *
 */
public class ActorUniverseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int threads = 20;
		
		ActorUniverse universe = new ActorUniverse("local", threads);
		
		List<Player> actors = new LinkedList<Player>();
		int i;
		for (i = 0; i < threads * 10; i++) {
			Player p = new Player(3200, 3200);
			universe.setupActor(p, new StandardPlayerHandler<Player>(p));
			actors.add(p);
		}
		for (i = 0; i < 10; i++) {
			for (Player p : actors) {
				for (Player o : actors) {
					if (p == o) {
						continue;
					}
					p.sendMessage(new PlayerMoveMessage<Player>(o, p, null, 1, 1));
				}
			}
		}
	}

}
