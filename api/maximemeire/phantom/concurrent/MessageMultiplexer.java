package maximemeire.phantom.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import maximemeire.phantom.task.Task;

/** 
 * @author Maxime Meire
 *
 */
public class MessageMultiplexer<T extends Actor> {
	
	private final int id;
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	public MessageMultiplexer(int id) {
		this.id = id;
	}
	
	public Task<?> registerTask(Task<?> task) {
		executor.execute(task);
		return task;
	}
	
	public Task<?> registerTask(Task<?> task, int delay) {
		ScheduledFuture<?> future = executor.schedule(task, delay, TimeUnit.MILLISECONDS);
		task.setScheduledFuture(future);
		return task;
	}
	
	public Task<?> registerTask(Task<?> task, int initialDelay, int interval) {
		ScheduledFuture<?> future = executor.scheduleWithFixedDelay(task, initialDelay, interval, TimeUnit.MILLISECONDS);
		task.setScheduledFuture(future);
		return task;
	}
	
	public Task<?> registerTask(Task<?> task, int initialDelay, int interval, TimeUnit timeUnit) {
		ScheduledFuture<?> future = executor.scheduleWithFixedDelay(task, initialDelay, interval, timeUnit);
		task.setScheduledFuture(future);
		return task;
	}
	
	public void send(final Message<?> message) {
		executor.execute(message);
	}

	public int getId() {
		return id;
	}
	
	public void shutDown() {
		executor.shutdown();
	}
	
}
