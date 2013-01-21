package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.concurrent.Actor;
import maximemeire.phantom.concurrent.MessageDispatcher;
import maximemeire.phantom.concurrent.annotations.OnMessage;

public class PlayerMessageDispatcher<A extends Player> extends MessageDispatcher<A> {

	public PlayerMessageDispatcher(A actor) {
		super(actor);
	}
	
	@OnMessage(type = DecodePacketMessage.class)
	public <Sender extends Actor, Receiver extends Player> void decodePacket(DecodePacketMessage<Receiver> message) throws Exception {
		actor.decodePacket(message);
	}
	
	@OnMessage(type = EncodePacketMessage.class)
	public <Sender extends Actor, Receiver extends Player> void encodePacket(EncodePacketMessage<Receiver> message) {
		actor.encodePacket(message);
	}
	
}
