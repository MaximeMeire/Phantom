package maximemeire.phantom.model.entity.player;

import org.jboss.netty.buffer.ChannelBuffer;

import maximemeire.phantom.concurrent.Message;

public class DecodePacketMessage<T extends Player> extends Message<T> {
	
	public final int opcode;
	public final int size;
	public final ChannelBuffer body;

	public DecodePacketMessage(int opcode, int size, ChannelBuffer body, T sender, T receiver, Runnable future) {
		super(sender, receiver, future);
		this.opcode = opcode;
		this.size = size;
		this.body = body;
	}

}
