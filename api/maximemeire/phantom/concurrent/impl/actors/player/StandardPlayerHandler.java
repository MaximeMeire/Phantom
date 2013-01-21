package maximemeire.phantom.concurrent.impl.actors.player;

import maximemeire.phantom.concurrent.Actor;
import maximemeire.phantom.concurrent.MessageDispatcher;
import maximemeire.phantom.concurrent.annotations.OnHandlerSwap;
import maximemeire.phantom.concurrent.annotations.OnKill;
import maximemeire.phantom.concurrent.annotations.OnMessage;

/**
 * Testing purpose.
 * @author Maxime Meire.
 *
 * @param <A> The {@link Actor} type.
 */
public class StandardPlayerHandler<A extends Player> extends MessageDispatcher<A> {

	public StandardPlayerHandler(A actor) {
		super(actor);
	}	
	
	@OnMessage(type = PlayerMoveMessage.class)
	public void onMove(final PlayerMoveMessage<Player> message) {
		actor.move(message);
		Runnable future = new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getId() + " " + message.getSenderReference().getAddress().toString() + " future run");
			}
		};
		System.out.println("Thread id = " + Thread.currentThread().getId() + " " + actor.getAddress() + " moved [" + message.getX() + ", " + message.getY() + "]");
		message.reply(new PlayerMoveMessage<Player>(1, 1, actor.getLink(message.getSenderReference()), future));
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
