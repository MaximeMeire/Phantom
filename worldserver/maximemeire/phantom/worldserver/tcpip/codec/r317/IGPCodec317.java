package maximemeire.phantom.worldserver.tcpip.codec.r317;

import maximemeire.phantom.network.tcpip.Codec;
import maximemeire.phantom.worldserver.tcpip.codec.AnonymousPacketDecoder;
import maximemeire.phantom.worldserver.tcpip.codec.AnonymousPacketEncoder;

public class IGPCodec317 extends Codec {
	
	private static final int REVISION = 317;
	
	public static Codec getCodec() {
		Codec codec = new PLPCodec317();
		for (int i = 0; i < codec.decoders.length; i++) {
			codec.decoders[i] = new AnonymousPacketDecoder(i);
		}
		for (int i = 0; i < codec.encoders.length; i++) {
			codec.encoders[i] = new AnonymousPacketEncoder(i);
		}

		return codec;
	}

	@Override
	public int getRevision() {
		return REVISION;
	}

}
