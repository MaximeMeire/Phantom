package maximemeire.phantom.model.map.message;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.model.entity.Entity;
import maximemeire.phantom.model.map.Location;
import maximemeire.phantom.model.map.Segment;

public class FinishRegistrationMessage<Receiver extends Segment> extends Message<Receiver> {
	
	public final Entity entity;
	public final Location location;
	
	public FinishRegistrationMessage(Entity entity, Location location, Link link) {
		super(link);
		this.entity = entity;
		this.location = location;
	}

	public FinishRegistrationMessage(Entity entity, Location location, Link link, Runnable future) {
		super(link, future);
		this.entity = entity;
		this.location = location;
	}
	
}
