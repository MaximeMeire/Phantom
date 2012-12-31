package maximemeire.phantom.concurrent;

import java.util.ArrayList;
import java.util.List;

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
			multiplexer.start();
		}
	}
	
	/**
	 * This is the only way actors can be initialized to be ready for message passing.
	 * @param <T>
	 * @param actor The {@link Actor} subclass to setup.
	 * @param handler The {@link MessageHandler} to handle messages with initially.
	 * @return A ready to use {@link Actor}.
	 */
	public <T extends Actor> T setupActor(T actor, MessageHandler<T> handler) {
		MessageMatcher matcher = new MessageMatcher(handler);
		MessageMultiplexer<?> multiplexer = multiplexers.get(currentMultiplexer++);
		currentMultiplexer %= threadCount;
		actor.setupActor(this, matcher, multiplexer);
		System.out.println(actor.getAddress() + " created on multiplexer " + multiplexer.getId());
		return actor;
	}

	/**
	 * Returns the name of this universe.
	 * @return
	 */
	public String getName() {
		return name;
	}	

}
