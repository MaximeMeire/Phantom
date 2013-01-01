package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.plp;

import maximemeire.phantom.model.entity.player.LoginDetails;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.worldserver.tcpip.codec.PacketEncoder;

public class FinishLoginRequestEncoder extends PacketEncoder {

	public FinishLoginRequestEncoder(int opcode, int size, HeaderType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		int responseCode = player.login((LoginDetails) context);
		DynamicOutboundBuffer buffer = new DynamicOutboundBuffer(3);
		buffer.put8(responseCode);
		switch (responseCode) {
		case 2:
			buffer.put8(player.getRights()).put8(0);
		}
		player.getChannel().write(buffer);
		if (responseCode == 2) {
			// TODO send map region etc

		}
	}

}
