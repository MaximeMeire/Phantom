package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.concurrent.Message;

public class EncodePacketMessage<T extends Player> extends Message<T> {
	
	public final int opcode;
	public final int size;
	public final Object context;

	public EncodePacketMessage(int opcode, int size, Object context, T sender, T receiver, Runnable future) {
		super(sender, receiver, future);
		this.opcode = opcode;
		this.size = size;
		this.context = context;
	}

}
