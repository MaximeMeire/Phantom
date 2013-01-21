package maximemeire.phantom.worldserver.tcpip.codec.r317.upstream.prelogin;

import maximemeire.phantom.model.entity.player.LoginDetails;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.InboundBuffer;
import maximemeire.phantom.network.PacketDecoder;
import maximemeire.phantom.util.Logging;
import maximemeire.phantom.worldserver.tcpip.codec.r317.PLCodec317;

import org.apache.log4j.Logger;

public class FreshLoginRequestDecoder extends PacketDecoder {
	
	private final static Logger LOGGER = Logging.log();

	public FreshLoginRequestDecoder(int opcode, int size) {
		super(opcode, size);
	}

	@SuppressWarnings("unused")
	@Override
	public void decode(Player player, int size, InboundBuffer buffer) throws Exception {
		if (size != buffer.readableBytes())
			throw new Exception("Too many/few bytes in buffer..");
		int magicNumber = buffer.readu8();
		if (magicNumber != 0xff)
			throw new Exception("Magic number incorrect");
		int revision = buffer.readu16();
		if (revision != player.getCodecRevision())
			throw new Exception("Incorrect revision field");
		boolean lowMemory = buffer.read8() == 1;
		int[] crcChecksums = new int[9];
		for (int i = 0; i < crcChecksums.length; i++) 
			crcChecksums[i] = buffer.read32();
		int rsaBlockSize = buffer.readu8();
		if (rsaBlockSize > buffer.readableBytes())
			throw new Exception("Error in RSA block");
		byte[] rsaBuffer = new byte[rsaBlockSize];
		buffer.readBytes(rsaBuffer);
		InboundBuffer rsaBlock = InboundBuffer.decryptRSA(rsaBuffer);
		int rsaMagicNumber = rsaBlock.read8();
		int[] sessionKeys = new int[4];
		for (int i = 0; i < 4; i++)
			sessionKeys[i] = rsaBlock.read32();
		if (((sessionKeys[2] << 32) | sessionKeys[3]) != player.getServerKey()) 
			throw new Exception("Non matching server session keys!");
		int uid = rsaBlock.read32();
		String username = rsaBlock.readString();
		String password = rsaBlock.readString();
		
		LOGGER.info("Decoded FRESH LOGIN for USERNAME=" + username + " PASSWORD=" + password);	
		player.encodePacket(PLCodec317.ENCODE_FINISH_LOGIN, new LoginDetails(sessionKeys, username, password));
	}

}
