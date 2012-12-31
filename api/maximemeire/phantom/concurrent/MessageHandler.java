package maximemeire.phantom.concurrent;

/**
 * @author Maxime Meire
 *
 * @param <T>
 */
public abstract class MessageHandler<T extends Actor> {
	
	protected T actor;
	
	/**
	 * Empty constructor used when it's needed to inject the ActorCreator and Address instance.
	 */
	public MessageHandler(T actor) {
		this.actor = actor;
	}
	
	public T getActor() {
		return this.actor;
	}

}
