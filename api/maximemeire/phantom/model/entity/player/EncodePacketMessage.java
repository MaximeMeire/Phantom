package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.network.PacketEncoder;

public class EncodePacketMessage<Receiver extends Player> extends Message<Receiver> {
	
	public final PacketEncoder encoder;
	public final Object context;
	
	public EncodePacketMessage(PacketEncoder encoder, Object context, Link link) {
		super(link);
		this.encoder = encoder;
		this.context = context;
	}

	public EncodePacketMessage(PacketEncoder encoder, Object context, Link link, Runnable future) {
		super(link, future);
		this.encoder = encoder;
		this.context = context;
	}

}
