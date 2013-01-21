package maximemeire.phantom.worldserver.tcpip.codec.r317.upstream.prelogin;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.InboundBuffer;
import maximemeire.phantom.network.PacketDecoder;
import maximemeire.phantom.util.Logging;
import maximemeire.phantom.worldserver.tcpip.codec.r317.PLCodec317;

import org.apache.log4j.Logger;

public class LoginHandshakeDecoder extends PacketDecoder {
	
	private final static Logger LOGGER = Logging.log();

	public LoginHandshakeDecoder(int opcode, int size) {
		super(opcode, size);
	}

	@Override
	public void decode(Player player, int size, InboundBuffer buffer) throws Exception {
		int a = buffer.readu8();
		LOGGER.info("Received LOGIN HANDSHAKE from " + player.getChannel().getRemoteAddress().toString());
		player.encodePacket(PLCodec317.ENCODE_LOGIN_HANDSHAKE, a);	
	}

}
