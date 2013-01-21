package maximemeire.phantom.concurrent.impl.actors.player;

import maximemeire.phantom.concurrent.Actor;
import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
/**
 * Testing purpose class.
 * @author Maxime Meire
 *
 * @param <Sender> The {@link Actor} sender type.
 * @param <Receiver> The {@link Actor} receiver type.
 */
public class PlayerMoveMessage<Receiver extends Player> extends Message<Receiver> {
	
	private int x;
	private int y;

	public PlayerMoveMessage(int x, int y, Link link) {
		super(link);
		this.x = x;
		this.y = y;
	}
	
	public PlayerMoveMessage(int x, int y, Link link, Runnable future) {
		super(link, future);
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
