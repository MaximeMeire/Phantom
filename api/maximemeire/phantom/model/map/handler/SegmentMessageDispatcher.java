package maximemeire.phantom.model.map.handler;

import maximemeire.phantom.concurrent.MessageDispatcher;
import maximemeire.phantom.concurrent.annotations.OnMessage;
import maximemeire.phantom.model.map.Segment;
import maximemeire.phantom.model.map.message.LoadSegmentMessage;

public class SegmentMessageDispatcher<A extends Segment> extends MessageDispatcher<A> {

	public SegmentMessageDispatcher(A actor) {
		super(actor);
	}
	
	@OnMessage(type = LoadSegmentMessage.class)
	public void loadSegment(LoadSegmentMessage<Segment> message) {
		actor.loadSegment(message);
	}
	
}
