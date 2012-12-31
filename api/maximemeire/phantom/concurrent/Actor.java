package maximemeire.phantom.concurrent;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Maxime Meire
 *
 */
public abstract class Actor {
	
	/**
	 * The {@link Address} of the actor.
	 */
	protected Address address;
	/**
	 * The {@link ActorUniverse} this actor belongs to.
	 */
	protected ActorUniverse universe;
	/**
	 * The {@link MessageMatcher} this actor uses to dispatch messages to the current
	 * {@link MessageHandler}
	 */
	protected MessageMatcher matcher;
	/**
	 * The {@link MessageMultiplexer} this actor belongs to.
	 */
	protected MessageMultiplexer<?> multiplexer;
	/**
	 * The list of linked {@link Actor} objects. Linked actors are killed too when this
	 * actor is killed.
	 */
	protected List<Actor> links = new LinkedList<Actor>();
	/**
	 * Whether this actor can execute actions.
	 */
	private boolean active = false;
	
	public Object futureLock = new Object();
	
	public Actor() { }
	
	/**
	 * 
	 * @param universe
	 * @param matcher
	 * @param multiplexer
	 */
	protected void setupActor(ActorUniverse universe, MessageMatcher matcher, MessageMultiplexer<?> multiplexer) {
		synchronized (this) {
			this.universe = universe;
			this.matcher = matcher;
			this.multiplexer = multiplexer;
			this.address = Address.newAddress(universe.getName(), this.getClass().getSimpleName() + getIdCounter());
			this.active = true;
		}
	}

	/**
	 * Returns the ID of a newly created sub actor class.
	 * @return The ID of a newly created sub actors class.
	 */
	protected abstract int getIdCounter();

	/**
	 * Send a {@link Message} to this actor.
	 * @param message The {@link Message} to send to this actor.
	 */
	public <T extends Actor> void sendMessage(Message<T> message) {
		multiplexer.send(message);
	}
	
	/**
	 * Deliver a {@link Message} to this actor.
	 * @param message The {@link Message} to send to this actor.
	 * @throws Exception TODO undefined
	 */
	public <T extends Actor> Object deliverMessage(Message<T> message) {
		return matcher.onMessageDelivery(message);
	}
	
	/**
	 * Execute the handler its handler swap logic.
	 */
	public void swapHandler() {
		synchronized (this) {
			matcher.onHandlerSwap();
		}
	}

	/**
	 * Links an actor to this actor. This is not a bidirectional link. Links are
	 * used to kill actors in a chain. If this actor is killed it will call its
	 * linked actors kill method too.
	 * @param <T> The {@link Actor} type.
	 * @param actor The {@link Actor} to link.
	 */
	public <T extends Actor> void link(T actor) {
		synchronized (links) {
			links.add(actor);
		}
	}

	/**
	 * Unlinks an actor to this actor. 
	 * @param <T> The {@link Actor} type.
	 * @param actor The {@link Actor} to link.
	 */
	public <T extends Actor> void unlink(T actor) {
		synchronized (links) {
			links.remove(actor);
		}
	}
	
	/**
	 * Kills the actor. ONLY to be called within a handler method in the {@link MessageHandler}.
	 * NEVER call this method outside the {@link MessageHandler} linked to this actor.
	 */
	public void kill() {
		active = false;
		matcher.onKill();
	}
	
	public ActorUniverse getUniverse() {
		return universe;
	}

	public Address getAddress() {
		return address;
	}

	public boolean isActive() {
		return active;
	}
	
	public <T extends Actor> boolean sameMultiplexer(T actor) {
		return this.multiplexer.getId() == actor.multiplexer.getId();
	}
	
	/**
	 * To use the address string hash code.
	 */
	@Override
	public int hashCode() {
		return this.address.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof Actor) {
				return this.address.equals(((Actor) o).address);
			}
		}
		return false;
	}

}
