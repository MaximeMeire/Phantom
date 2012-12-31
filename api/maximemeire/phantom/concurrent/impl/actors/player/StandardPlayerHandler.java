package maximemeire.phantom.concurrent.impl.actors.player;

import maximemeire.phantom.concurrent.MessageHandler;
import maximemeire.phantom.concurrent.annotations.OnHandlerSwap;
import maximemeire.phantom.concurrent.annotations.OnKill;
import maximemeire.phantom.concurrent.annotations.OnMessage;

/**
 * Testing purpose.
 * @author Maxime Meire.
 *
 * @param <T>
 */
public class StandardPlayerHandler<T extends Player> extends MessageHandler<T> {

	public StandardPlayerHandler(T actor) {
		super(actor);
	}	
	
	@OnMessage(type = PlayerMoveMessage.class)
	public void onMove(final PlayerMoveMessage<Player> message) {
		actor.move(message);
		Runnable future = new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getId() + " " + message.getSender().getAddress().toString() + " future run");
			}
		};
		System.out.println("Thread id = " + Thread.currentThread().getId() + " " + actor.getAddress() + " moved [" + message.getX() + ", " + message.getY() + "]");
		message.getSender().sendMessage(new PlayerMoveMessage<Player>(actor, message.getSender(), future, 1, 1));
	}
	
	@OnKill
	public void onKill() {
		System.out.println("Thread id = " + Thread.currentThread().getId() + " " + actor.getAddress().toString() + " killed.");
	}
	
	@OnHandlerSwap
	public void onHandlerSwap() {
		System.out.println("Thread id = " + Thread.currentThread().getId() + " " + actor.getAddress().toString() + " swapping handler");
	}
	

}
