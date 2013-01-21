package maximemeire.phantom.network;

import org.apache.log4j.Logger;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.util.Logging;

public abstract class PacketEncoder {
	
	private static final Logger LOGGER = Logging.log();
	
	public static enum PacketType {
		BARE,
		FIXED,
		VAR_BYTE,
		VAR_SHORT
	}
	
	public final int opcode;
	public final int size;
	public final PacketType headerType;
	
	public PacketEncoder(int opcode, int size, PacketType headerType) {
		this.opcode = opcode;
		this.size = size;
		this.headerType = headerType;
	}
	
	public abstract void encode(Player player, Object context);
	
	/**
	 * This method must be called when writing a packet. It appends
	 * the header in the required format before writing the packet
	 * to the channel.
	 * @param player The {@link Player} to write to.
	 * @param payload The {@link DynamicOutboundBuffer} that represents the
	 * packet payload.
	 */
	protected void write(Player player, DynamicOutboundBuffer payload) {
		DynamicOutboundBuffer packet;
		switch (headerType) {
		case BARE:
			player.getChannel().write(payload);
			break;
		case FIXED:
			if (payload == null) {
				packet = new DynamicOutboundBuffer(1);
				packet.put8(opcode + player.getOutboundCipher().getNextValue());
				player.getChannel().write(packet);
			} else {
				packet = new DynamicOutboundBuffer(payload.readableBytes() + 1);
				packet.put8(opcode + player.getOutboundCipher().getNextValue());
				packet.writeBytes(payload);
				player.getChannel().write(packet);
			}
			LOGGER.info("Written " + this.getClass().getSimpleName() + " to " + player.getChannel().getRemoteAddress());
			break;
		case VAR_BYTE:
			packet = new DynamicOutboundBuffer(payload.readableBytes() + 2);
			packet.put8(opcode + player.getOutboundCipher().getNextValue());
			packet.put8(payload.readableBytes());
			packet.writeBytes(payload);
			player.getChannel().write(packet);
			LOGGER.info("Written " + this.getClass().getSimpleName() + " to " + player.getChannel().getRemoteAddress());
			break;
		case VAR_SHORT:
			packet = new DynamicOutboundBuffer(payload.readableBytes() + 3);
			packet.put8(opcode + player.getOutboundCipher().getNextValue());
			packet.put16(payload.readableBytes());
			packet.writeBytes(payload);
			player.getChannel().write(packet);
			LOGGER.info("Written " + this.getClass().getSimpleName() + " to " + player.getChannel().getRemoteAddress());
			break;
		}
	}

}
