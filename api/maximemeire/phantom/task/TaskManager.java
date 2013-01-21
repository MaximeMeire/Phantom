package maximemeire.phantom.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import maximemeire.phantom.concurrent.Actor;

public class TaskManager {
	
	private Actor actor;
	private Map<String, Task<?>> tasks = new ConcurrentHashMap<String, Task<?>>();
	
	public TaskManager(Actor actor) {
		this.actor = actor;
	}
	
	public Task<?> getTask(String name) {
		Task<?> task = tasks.get(name);
		if (task == null) {
			try {
				throw new Exception("Trying to retrieve an unregistered task");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return task;
	}
	
	public void registerTask(String name, Task<?> task) {
		if (task.scheduleFuture && name != null) {
			tasks.put(name, actor.getMultiplexer().registerTask(task));
		} else {
			actor.getMultiplexer().registerTask(task);
		}
	}
	
	public void registerTask(String name, Task<?> task, int delay) {
		if (task.scheduleFuture && name != null) {
			tasks.put(name, actor.getMultiplexer().registerTask(task, delay));
		} else {
			actor.getMultiplexer().registerTask(task, delay);
		}
	}
	
	public void registerTask(String name, Task<?> task, int delay, int interval) {
		if (task.scheduleFuture && name != null) {
			tasks.put(name, actor.getMultiplexer().registerTask(task, delay, interval));
		} else {
			actor.getMultiplexer().registerTask(task, delay, interval);
		}
	}
	
	public void registerTask(String name, Task<?> task, int initialDelay, int interval, TimeUnit timeUnit) {
		if (task.scheduleFuture && name != null) {
			tasks.put(name, actor.getMultiplexer().registerTask(task, initialDelay, interval, timeUnit));
		} else {
			actor.getMultiplexer().registerTask(task, initialDelay, interval, timeUnit);
		}
	}

}
