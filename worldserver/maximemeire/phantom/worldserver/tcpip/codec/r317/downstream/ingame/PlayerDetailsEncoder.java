package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.network.PacketEncoder;

public class PlayerDetailsEncoder extends PacketEncoder {

	public PlayerDetailsEncoder(int opcode, int size, PacketType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		DynamicOutboundBuffer payload = new DynamicOutboundBuffer(3);
		payload.put8_p128(player.isMember() ? 1 : 0).put16_le_p128(player.getIndex());
		write(player, payload);
	}

}
