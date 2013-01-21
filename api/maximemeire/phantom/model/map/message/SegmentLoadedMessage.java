package maximemeire.phantom.model.map.message;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.model.map.Region;

public class SegmentLoadedMessage<Receiver extends Region> extends Message<Receiver> {

	public SegmentLoadedMessage(Link link) {
		super(link);
	}
	
	public SegmentLoadedMessage(Link link, Runnable future) {
		super(link, future);
	}

}
