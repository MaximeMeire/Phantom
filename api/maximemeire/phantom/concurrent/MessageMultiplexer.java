package maximemeire.phantom.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/** 
 * @author Maxime Meire
 *
 */
public class MessageMultiplexer<T extends Actor> extends Thread {
	
	private final int id;
	private boolean active = true;
	private final BlockingQueue<Message<?>> queue = new LinkedBlockingQueue<Message<?>>();
	
	public MessageMultiplexer(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		while (active) {
			try {
				Message<?> message = queue.take();
				message.deliver();
			} catch (InterruptedException e) {
				e.printStackTrace();
				shutDown();
			} catch (Exception e) {
				e.printStackTrace();
				shutDown();
			}
		}
	}
	
	public void send(Message<?> message) {
		queue.offer(message);
	}

	public int getMultiplexerId() {
		return id;
	}
	
	public void shutDown() {
		this.active = false;
	}
	
}
