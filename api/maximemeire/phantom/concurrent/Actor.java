package maximemeire.phantom.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import maximemeire.phantom.task.Task;
import maximemeire.phantom.task.TaskManager;

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
	 * {@link MessageDispatcher}
	 */
	protected MessageMatcher matcher;
	/**
	 * The {@link MessageMultiplexer} this actor belongs to.
	 */
	protected MessageMultiplexer<?> multiplexer;
	/**
	 * The {@link TaskManager} this actor uses to execute scheduled tasks.
	 */
	protected TaskManager taskManager;
	/**
	 * The list of linked {@link Actor} objects. Linked actors are killed too when this
	 * actor is killed.
	 */
	protected List<Actor> links = new LinkedList<Actor>();
	/**
	 * Whether this actor can execute actions.
	 */
	private boolean active = false;
	
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
			this.address = Address.newAddress(universe.getName(), this.getClass().getSimpleName() + getActorId());
			this.taskManager = new TaskManager(this);
			this.active = true;
		}
	}
	
	public Link getLink(Actor receiver) {
		return new Link(this, receiver);
	}

	/**
	 * Returns the ID of a newly created sub actor class.
	 * @return The ID of a newly created sub actor class.
	 */
	protected abstract int getActorId();
	
	public void registerTask(String name, Task<?> task) {
		taskManager.registerTask(name, task);
	}
	
	public void registerTask(String name, Task<?> task, int delay) {
		taskManager.registerTask(name, task, delay);
	}
	
	public void registerTask(String name, Task<?> task, int delay, int interval) {
		taskManager.registerTask(name, task, delay, interval);
	}
	
	public void registerTask(String name, Task<?> task, int initialDelay, int interval, TimeUnit timeUnit) {
		taskManager.registerTask(name, task, initialDelay, interval, timeUnit);
	}

	/**
	 * Send a {@link Sender} to this actor.
	 * @param Sender The {@link Sender} to send to this actor.
	 */
	public <Receiver extends Actor> void sendMessage(Message<Receiver> message) {
		universe.sendMessage(message);//multiplexer.send(message);
	}
	
	/**
	 * Deliver a {@link Message} to this actor.
	 * @param message The {@link Message} to send to this actor.
	 * @throws Exception TODO undefined
	 */
	public <Receiver extends Actor> Object deliverMessage(Message<Receiver> message) {
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
	 * Kills the actor. ONLY to be called within a handler method in the {@link MessageDispatcher}.
	 * NEVER call this method outside the {@link MessageDispatcher} linked to this actor.
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
	
	public MessageMultiplexer<?> getMultiplexer() {
		return multiplexer;
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
