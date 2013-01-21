package maximemeire.phantom.concurrent;


/**
 * This class represents a message send across {@link Actor} object.
 * @author Maxime Meire
 *
 * @param <Sender> The {@link Actor} sender type.
 * @param <Receiver> The {@link Actor} receiver type.
 */
public abstract class Message<Receiver extends Actor> implements Runnable {

	/**
	 * The {@link Address} of the actor sending this message. 
	 */
	private final Actor sender;
	/**
	 * The {@link Actor} receiving this message.
	 */
	private final Receiver receiver;
	/**
	 * The {@link Runnable} to execute when the message logic has been executed.
	 */
	private final Runnable future;
	
	@SuppressWarnings("unchecked")
	public Message(final Link link) {
		this.sender = link.sender;
		this.receiver = (Receiver) link.receiver;
		this.future = null;
	}
	
	@SuppressWarnings("unchecked")
	public Message(final Link link, final Runnable future) {
		this.sender = link.sender;
		this.receiver = (Receiver) link.receiver;
		this.future = future;
	}
	
	@Override
	public void run() {
		try {
			deliver();
		} catch (InterruptedException e) {
			e.printStackTrace();
			receiver.multiplexer.shutDown();
		} catch (Exception e) {
			e.printStackTrace();
			receiver.multiplexer.shutDown();
		}
	}
	
	public final void deliver() throws Exception {
		receiver.universe.deliverMessage(this); 
		if (future != null) {
			future.run();
		}
	}
	
	public final void reply(Message<?> message) {
		sender.universe.sendMessage(message);
	}
	
	/**
	 * This method should only be used if a reference is needed
	 * to the sender its object. Don't ever use this to access
	 * state, unless you are sure you are accessing read only
	 * data, i.e. final primitive types. Never 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Actor> T getSenderReference() {
		return (T) sender;
	}
	
	public final Receiver getReceiver() {
		return receiver;
	}
	
}
