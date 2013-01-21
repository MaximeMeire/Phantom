package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.prelogin;

import maximemeire.phantom.model.entity.player.LoginDetails;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.network.PacketEncoder;
import maximemeire.phantom.worldserver.model.ui.SidebarInterface;
import maximemeire.phantom.worldserver.tcpip.codec.r317.IGCodec317;

public class FinishLoginEncoder extends PacketEncoder {

	public FinishLoginEncoder(int opcode, int size, PacketType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		boolean flagged = false;
		
		int responseCode = player.login((LoginDetails) context);
		DynamicOutboundBuffer payload = new DynamicOutboundBuffer(3);
		payload.put8(responseCode);
		switch (responseCode) {
		case 2:
			payload.put8(player.getRights()).put8(flagged ? 1 : 0);
		}
		write(player, payload);
		if (responseCode == 2) {
			player.encodePacket(IGCodec317.ENCODE_PLAYER_DETAILS, null);
			player.encodePacket(IGCodec317.ENCODE_RESET_CAMERA, null);
			player.encodePacket(IGCodec317.ENCODE_MESSAGE, "Welcome to Maxi's 317 RSPS powered by the Phantom Actor API.");
			player.encodePacket(IGCodec317.ENCODE_MAP_REGION, null);
			player.encodePacket(IGCodec317.ENCODE_SIDEBAR_INTERFACE, SidebarInterface.STANDARD_SIDEBAR);
		}
	}

}
