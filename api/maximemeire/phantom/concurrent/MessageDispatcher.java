package maximemeire.phantom.concurrent;

/**
 * @author Maxime Meire
 *
 * @param <A>
 */
public abstract class MessageDispatcher<A extends Actor> {

	protected A actor;
	
	/**
	 * Empty constructor used when it's needed to inject the ActorCreator and Address instance.
	 */
	public MessageDispatcher(A actor) {
		this.actor = actor;
	}
	
	public A getActor() {
		return this.actor;
	}
	
	protected ActorUniverse getUniverse() {
		return actor.universe;
	}

}
