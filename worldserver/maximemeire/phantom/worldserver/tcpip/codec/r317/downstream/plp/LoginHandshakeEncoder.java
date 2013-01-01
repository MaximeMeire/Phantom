package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.plp;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.worldserver.tcpip.codec.PacketEncoder;

public class LoginHandshakeEncoder extends PacketEncoder {
	
	private static final DynamicOutboundBuffer HANDSHAKE_OK = new DynamicOutboundBuffer(new byte[] { (byte) 0, 0, 0, 0, 0, 0, 0, 0, 0 });

	public LoginHandshakeEncoder(int opcode, int size, HeaderType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		DynamicOutboundBuffer buffer = new DynamicOutboundBuffer(size);
		buffer.writeBytes(HANDSHAKE_OK);
		int a = (int) (Math.random() * 99999999D);
		int b = (int) (Math.random() * 99999999D);
		buffer.put32(a).put32(b);
		player.getChannel().write(buffer);
	}

}
