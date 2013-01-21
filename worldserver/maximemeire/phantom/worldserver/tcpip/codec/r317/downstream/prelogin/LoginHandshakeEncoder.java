package maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.prelogin;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.network.PacketEncoder;

public class LoginHandshakeEncoder extends PacketEncoder {
	
	private static final DynamicOutboundBuffer HANDSHAKE_NOT_OK = new DynamicOutboundBuffer(new byte[] { (byte) 0, 0, 0, 0, 0, 0, 0, 0, -1 });
	private static final DynamicOutboundBuffer HANDSHAKE_OK = new DynamicOutboundBuffer(new byte[] { (byte) 0, 0, 0, 0, 0, 0, 0, 0, 0 });

	public LoginHandshakeEncoder(int opcode, int size, PacketType headerType) {
		super(opcode, size, headerType);
	}

	@Override
	public void encode(Player player, Object context) {
		int a = (Integer) context;
		if (a != 0) {
			DynamicOutboundBuffer buffer = new DynamicOutboundBuffer(size);
			buffer.writeBytes(HANDSHAKE_OK);
			int s1 = (int) (Math.random() * 99999999D);
			int s2 = (int) (Math.random() * 99999999D);
			long serverKey = (s1 << 32) | s2;
			player.setServerKey(serverKey);
			buffer.put32(s1).put32(s2);
			write(player, buffer);
		} else {
			write(player, HANDSHAKE_NOT_OK);
		}
	}

}
