package maximemeire.phantom.model.map.message;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.model.map.Location;
import maximemeire.phantom.model.map.WorldMap;

public class RegisterEntityMessage<Receiver extends WorldMap>extends Message<Receiver> {
	
	public final Location location;
	
	public RegisterEntityMessage(Location location, Link link) {
		super(link);
		this.location = location;
	}

	public RegisterEntityMessage(Location location, Link link, Runnable future) {
		super(link, future);
		this.location = location;
	}

}
