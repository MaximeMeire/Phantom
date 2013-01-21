package maximemeire.phantom.model.map.message;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.model.map.Segment;

public class LoadSegmentMessage<Receiver extends Segment> extends Message<Receiver> {

	public LoadSegmentMessage(Link link) {
		super(link);
	}
	
	public LoadSegmentMessage(Link link, Runnable future) {
		super(link, future);
	}

}
