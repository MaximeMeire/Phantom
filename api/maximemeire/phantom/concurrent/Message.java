package maximemeire.phantom.concurrent;


/**
 * This class represents a message send across {@link Actor} object.
 * @author Maxime Meire
 *
 * @param <T> The {@link Actor} type.
 */
public abstract class Message<T extends Actor> {

	private final T sender;
	private final T receiver;
	private final Runnable future;
	
	public Message(T sender, T receiver, Runnable future) {
		this.sender = sender;
		this.receiver = receiver;
		this.future = future;
	}
	
	public void deliver() throws Exception {
		receiver.deliverMessage(this);
		if (future != null) {
			future.run();
		}
	}

	public T getSender() {
		return sender;
	}
	
	public T getReceiver() {
		return receiver;
	}
	
}
