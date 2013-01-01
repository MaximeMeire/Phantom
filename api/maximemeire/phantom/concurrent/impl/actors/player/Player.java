package maximemeire.phantom.concurrent.impl.actors.player;

import maximemeire.phantom.concurrent.Actor;

/**
 * Testing purpose.
 * @author Maxime Meire
 *
 */
public class Player extends Actor {
	
	private static int counter = 1;
	
	private int x;
	private int y;
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	protected int getActorIdCounter() {
		return counter++;
	}
	
	public void move(PlayerMoveMessage<Player> message) {
		this.x += message.getX();
		this.y += message.getY();
		swapHandler();
	}

}
