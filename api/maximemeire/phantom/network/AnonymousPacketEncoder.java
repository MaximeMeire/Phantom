package maximemeire.phantom.network;

import org.apache.log4j.Logger;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.util.Logging;

public class AnonymousPacketEncoder extends PacketEncoder {
	
	private final static Logger LOGGER = Logging.log();

	public AnonymousPacketEncoder(int opcode) {
		super(opcode, 0, null);
	}

	@Override
	public void encode(Player player, Object context) {
		LOGGER.info("Encoding UNKNOWN PACKET OPCODE=" + opcode);
	}

}
