package maximemeire.phantom.worldserver.tcpip.codec;

import maximemeire.phantom.model.entity.player.Player;

public abstract class PacketEncoder {
	
	public static enum HeaderType {
		BARE,
		FIXED,
		VAR_BYTE,
		VAR_SHORT
	}
	
	public final int opcode;
	public final int size;
	public final HeaderType headerType;
	
	public PacketEncoder(int opcode, int size, HeaderType headerType) {
		this.opcode = opcode;
		this.size = size;
		this.headerType = headerType;
	}
	
	public abstract void encode(Player player, Object context);

}
