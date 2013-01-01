package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.concurrent.MessageHandler;
import maximemeire.phantom.concurrent.annotations.OnMessage;

public class PlayerMessageHandler<T extends Player> extends MessageHandler<T> {

	public PlayerMessageHandler(T actor) {
		super(actor);
	}
	
	@OnMessage(type = DecodePacketMessage.class)
	public void decodePacket(DecodePacketMessage<T> message) throws Exception {
		actor.decodePacket(message);
	}
	
	@OnMessage(type = EncodePacketMessage.class)
	public void encodePacket(EncodePacketMessage<T> message) {
		actor.encodePacket(message);
	}
	
}
