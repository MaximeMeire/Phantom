package maximemeire.phantom.model.map.handler;

import maximemeire.phantom.concurrent.MessageDispatcher;
import maximemeire.phantom.concurrent.annotations.OnMessage;
import maximemeire.phantom.model.map.Region;
import maximemeire.phantom.model.map.message.LoadRegionMessage;
import maximemeire.phantom.model.map.message.SegmentLoadedMessage;

public class RegionMessageDispatcher<A extends Region> extends MessageDispatcher<A> {

	public RegionMessageDispatcher(A actor) {
		super(actor);
	}
	
	@OnMessage(type = LoadRegionMessage.class)
	protected void loadRegion(LoadRegionMessage<Region> message) {
		actor.loadRegion(message); 
	}
	
	@OnMessage(type = SegmentLoadedMessage.class)
	protected void segmentLoaded(SegmentLoadedMessage<Region> message) {
		actor.segmentLoaded(message);
	}

}
