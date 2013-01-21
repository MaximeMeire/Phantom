package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.network.PacketEncoder;
import maximemeire.phantom.worldserver.model.ui.SidebarInterface;

public class SidebarInterfaceEncoder extends PacketEncoder {

	public SidebarInterfaceEncoder(int opcode, int size, PacketType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		SidebarInterface[] sidebar = (SidebarInterface[]) context;
		for (SidebarInterface i : sidebar) {
			DynamicOutboundBuffer payload = new DynamicOutboundBuffer(3);
			payload.put16(i.interfaceId).put8_p128(i.icon);
			write(player, payload);
		}
	}

}
