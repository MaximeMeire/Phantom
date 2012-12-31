package maximemeire.phantom.concurrent.impl.actors.player;

import maximemeire.phantom.concurrent.Message;
/**
 * Testing purpose class.
 * @author Maxime Meire
 *
 * @param <T>
 */
public class PlayerMoveMessage<T extends Player> extends Message<T> {
	
	private int x;
	private int y;

	public PlayerMoveMessage(T sender, T receiver, Runnable future, int x, int y) {
		super(sender, receiver, future);
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
