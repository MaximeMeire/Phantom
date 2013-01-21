package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.network.PacketEncoder;

public class MessageEncoder extends PacketEncoder {

	public MessageEncoder(int opcode, int size, PacketType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		DynamicOutboundBuffer payload = new DynamicOutboundBuffer(128);
		String message = (String) context;
		payload.put_s(message);
		write(player, payload);
	}

}
