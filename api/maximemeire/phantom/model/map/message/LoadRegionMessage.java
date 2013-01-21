package maximemeire.phantom.model.map.message;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.model.map.Region;

public class LoadRegionMessage<Receiver extends Region> extends Message<Receiver> {
	
	public LoadRegionMessage(Link link) {
		super(link);
	}	
	
	public LoadRegionMessage(Link link, Runnable future) {
		super(link, future);
	}	
	
}
