package maximemeire.phantom.worldserver.tcpip.codec.r317.upstream.plp;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.DynamicOutboundBuffer;
import maximemeire.phantom.network.InboundBuffer;
import maximemeire.phantom.util.Logging;
import maximemeire.phantom.worldserver.tcpip.codec.PacketDecoder;

import org.apache.log4j.Logger;

public class LoginHandshakeDecoder extends PacketDecoder {
	
	private static final DynamicOutboundBuffer HANDSHAKE_NOT_OK = new DynamicOutboundBuffer(new byte[] { (byte) 0, 0, 0, 0, 0, 0, 0, 0, -1 });
	
	private final static Logger LOGGER = Logging.log();

	public LoginHandshakeDecoder(int opcode, int size) {
		super(opcode, size);
	}

	@Override
	public void decode(Player player, int size, InboundBuffer buffer) throws Exception {
		int a = buffer.readu8();
		LOGGER.info("Received LOGIN HANDSHAKE from " + player.getChannel().getRemoteAddress().toString());
		if (a != 0)
			player.encodePacket(0, null);
		else
			player.getChannel().write(HANDSHAKE_NOT_OK);
			
	}

}
