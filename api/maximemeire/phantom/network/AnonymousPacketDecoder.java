package maximemeire.phantom.network;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.util.Logging;

import org.apache.log4j.Logger;

public class AnonymousPacketDecoder extends PacketDecoder {
	
	private final static Logger LOGGER = Logging.log();

	public AnonymousPacketDecoder(int opcode) {
		super(opcode, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void decode(Player player, int size, InboundBuffer buffer) {
		LOGGER.info("Decoding UNKNOWN PACKET OPCODE=" + opcode + " LENGTH=" + size);
	}

}
