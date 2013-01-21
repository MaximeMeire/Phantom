package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.PacketEncoder;

public class ResetCameraEncoder extends PacketEncoder {

	public ResetCameraEncoder(int opcode, int size, PacketType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		write(player, null);
	}

}
