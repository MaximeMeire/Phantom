package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.concurrent.Link;
import maximemeire.phantom.concurrent.Message;
import maximemeire.phantom.network.PacketDecoder;

import org.jboss.netty.buffer.ChannelBuffer;

public class DecodePacketMessage<Receiver extends Player> extends Message<Receiver> {
	
	public final PacketDecoder decoder;
	public final int size;
	public final ChannelBuffer body;
	
	public DecodePacketMessage(PacketDecoder decoder, int size, ChannelBuffer body, Link link) {
		super(link);
		this.decoder = decoder;
		this.size = size;
		this.body = body;
	}

	public DecodePacketMessage(PacketDecoder decoder, int size, ChannelBuffer body, Link link, Runnable future) {
		super(link, future);
		this.decoder = decoder;
		this.size = size;
		this.body = body;
	}

}
