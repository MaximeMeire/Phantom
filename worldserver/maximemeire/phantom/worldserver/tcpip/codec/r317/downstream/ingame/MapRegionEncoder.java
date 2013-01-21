package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.map.Location;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.network.PacketEncoder;

public class MapRegionEncoder extends PacketEncoder {

	public MapRegionEncoder(int opcode, int size, PacketType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		player.setClientLoadedBaseLocation();
		DynamicOutboundBuffer payload = new DynamicOutboundBuffer(4);
		Location location = player.getLocation();
		payload.put16_p128(location.getSegmentX()).put16(location.getSegmentY());
		write(player, payload);
	}

}
