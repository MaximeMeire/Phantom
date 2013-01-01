package maximemeire.phantom.worldserver.tcpip.codec.r317;

import maximemeire.phantom.network.tcpip.Codec;
import maximemeire.phantom.worldserver.tcpip.codec.AnonymousPacketDecoder;
import maximemeire.phantom.worldserver.tcpip.codec.AnonymousPacketEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.PacketEncoder.HeaderType;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.plp.FinishLoginRequestEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.plp.LoginHandshakeEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.upstream.plp.FreshLoginRequestDecoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.upstream.plp.LoginHandshakeDecoder;

public class PLPCodec317 extends Codec {
	
	private static final int REVISION = 317;

	public static Codec getCodec() {
		Codec codec = new PLPCodec317();
		for (int i = 0; i < codec.decoders.length; i++) {
			codec.decoders[i] = new AnonymousPacketDecoder(i);
		}
		for (int i = 0; i < codec.encoders.length; i++) {
			codec.encoders[i] = new AnonymousPacketEncoder(i);
		}
		
		codec.decoders[14] = new LoginHandshakeDecoder(14, 1);
		codec.decoders[16] = new FreshLoginRequestDecoder(16, -1);
		
		codec.encoders[0] = new LoginHandshakeEncoder(0, 17, HeaderType.BARE);
		codec.encoders[1] = new FinishLoginRequestEncoder(0, 0, HeaderType.BARE);
		return codec;
	}

	@Override
	public int getRevision() {
		return REVISION;
	}
	
}
