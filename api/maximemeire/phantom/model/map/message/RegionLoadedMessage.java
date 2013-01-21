package maximemeire.phantom.model.map.message;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.model.map.WorldMap;

public class RegionLoadedMessage<Receiver extends WorldMap> extends Message<Receiver> {
	
	public RegionLoadedMessage(Link link) {
		super(link);
	}

	public RegionLoadedMessage(Link link, Runnable future) {
		super(link, future);
	}

}
