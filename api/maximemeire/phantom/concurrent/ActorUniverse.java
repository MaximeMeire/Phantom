package maximemeire.phantom.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import maximemeire.phantom.util.Logging;

import org.apache.log4j.Logger;

/**
 * @author Maxime Meire
 *
 */
public class ActorUniverse {
	
	/**
	 * The name of this universe.
	 */
	private final String name;
	/**
	 * The amount of threads this universe is run on.
	 */
	private final int threadCount;
	/**
	 * The current thread that needs to be assigned
	 */
	private int currentMultiplexer = 0;
	/**
	 * The list of {@link MessageMultiplexer} objects that handle this universe.
	 */
	public List<MessageMultiplexer<?>> multiplexers = new ArrayList<MessageMultiplexer<?>>();
	/**
	 * The map of actors.
	 */
	public Map<Address, Actor> actors = new ConcurrentHashMap<Address, Actor>();
	
	private final static Logger LOGGER = Logging.log();
	
	/**
	 * Creates a new ActorUniverse instance.
	 * @param name The UNIQUE name of this universe.
	 * @param threadCount The number of threads assigned to this 
	 * @param multiplier
	 */
	public ActorUniverse(final String name, final int threadCount) {
		this.name = name;
		this.threadCount = threadCount;
		MessageMultiplexer<?> multiplexer;
		for (int i = 0; i < threadCount; i++) {
			multiplexer = new MessageMultiplexer<Actor>(i);
			multiplexers.add(multiplexer);
		}
	}
	
	/**
	 * This is the only way actors can be initialized to be ready for message passing.
	 * @param <T>
	 * @param actor The {@link Actor} subclass to setup.
	 * @param handler The {@link MessageDispatcher} to handle messages with initially.
	 * @return A ready to use {@link Actor}.
	 */
	public <T extends Actor> T setupActor(T actor, MessageDispatcher<T> handler) {
		MessageMatcher matcher = new MessageMatcher(handler);
		MessageMultiplexer<?> multiplexer = multiplexers.get(currentMultiplexer++);
		currentMultiplexer %= threadCount;
		actor.setupActor(this, matcher, multiplexer);
		actors.put(actor.address, actor);
		LOGGER.info(actor.getAddress() + " created on multiplexer " + multiplexer.getId());
		return actor;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Actor> T getActor(Address address) {
		return (T) actors.get(address);
	}
	
	public void sendMessage(Message<?> message) {
		try {
			Actor actor = actors.get(message.getReceiver().address);
			if (actor == null)
				throw new Exception("Actor is not registered");
			actor.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deliverMessage(Message<?> message) {
		try {
			Actor actor = actors.get(message.getReceiver().address);
			if (actor == null)
				throw new Exception("Actor is not registered");
			actor.deliverMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the name of this universe.
	 * @return
	 */
	public String getName() {
		return name;
	}	

}
