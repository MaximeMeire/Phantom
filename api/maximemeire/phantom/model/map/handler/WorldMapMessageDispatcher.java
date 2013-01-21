package maximemeire.phantom.model.map.handler;

import maximemeire.phantom.concurrent.MessageDispatcher;
import maximemeire.phantom.concurrent.annotations.OnMessage;
import maximemeire.phantom.model.map.WorldMap;
import maximemeire.phantom.model.map.message.RegionLoadedMessage;
import maximemeire.phantom.model.map.message.RegisterEntityMessage;

public class WorldMapMessageDispatcher<A extends WorldMap> extends MessageDispatcher<A> {

	public WorldMapMessageDispatcher(A actor) {
		super(actor);
	}
	
	@OnMessage(type = RegisterEntityMessage.class)
	public void registerEntity(RegisterEntityMessage<WorldMap> message) {
		actor.registerEntity(message);
	}
	
	@OnMessage(type = RegionLoadedMessage.class)
	public void regionLoaded(RegionLoadedMessage<WorldMap> message) {
		actor.regionRegistered(message);
	}

}
