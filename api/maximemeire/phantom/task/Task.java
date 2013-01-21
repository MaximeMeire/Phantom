package maximemeire.phantom.task;

import java.util.concurrent.ScheduledFuture;

import maximemeire.phantom.concurrent.Actor;

public abstract class Task<A extends Actor> implements Runnable {

	protected ScheduledFuture<?> scheduledFuture;
	protected boolean scheduleFuture;
	
	public Task(boolean scheduleFuture) {
		this.scheduleFuture = scheduleFuture;
	}
	
	protected void cancel() {
		if (scheduleFuture && scheduledFuture != null) {
			scheduledFuture.cancel(true);
		} else {
			try {
				throw new Exception("Trying to cancel a future that was never created.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		if (scheduleFuture) 
			this.scheduledFuture = scheduledFuture;
	}
	
	public boolean scheduleFuture() {
		return this.scheduleFuture;
	}

}
