package maximemeire.phantom.network;

import maximemeire.phantom.model.entity.player.Player;

public abstract class PacketDecoder {
	
	public final int opcode;
	public final int size;
	
	public PacketDecoder(int opcode, int size) {
		this.opcode = opcode;
		this.size = size;
	}
	
	public abstract void decode(Player player, int size, InboundBuffer buffer) throws Exception;

}
