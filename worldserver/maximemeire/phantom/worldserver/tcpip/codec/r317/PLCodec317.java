package maximemeire.phantom.worldserver.tcpip.codec.r317;

import maximemeire.phantom.network.AnonymousPacketDecoder;
import maximemeire.phantom.network.AnonymousPacketEncoder;
import maximemeire.phantom.network.PacketEncoder.PacketType;
import maximemeire.phantom.network.tcpip.Codec;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.prelogin.FinishLoginEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.prelogin.LoginHandshakeEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.upstream.prelogin.FreshLoginRequestDecoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.upstream.prelogin.LoginHandshakeDecoder;

public class PLCodec317 extends Codec {
	
	private static final int REVISION = 317;
	
	public static final int DECODE_LOGIN_HANDSHAKE = 14;
	public static final int DECODE_FRESH_LOGIN = 16;
	
	public static final int ENCODE_LOGIN_HANDSHAKE = 0;
	public static final int ENCODE_FINISH_LOGIN = 1;

	public static Codec getCodec() {
		Codec codec = new PLCodec317();
		for (int i = 0; i < codec.decoders.length; i++) {
			codec.decoders[i] = new AnonymousPacketDecoder(i);
		}
		for (int i = 0; i < codec.encoders.length; i++) {
			codec.encoders[i] = new AnonymousPacketEncoder(i);
		}
		
		codec.decoders[DECODE_LOGIN_HANDSHAKE] = new LoginHandshakeDecoder(DECODE_LOGIN_HANDSHAKE, 1);
		codec.decoders[DECODE_FRESH_LOGIN] = new FreshLoginRequestDecoder(DECODE_FRESH_LOGIN, -1);
		
		codec.encoders[ENCODE_LOGIN_HANDSHAKE] = new LoginHandshakeEncoder(0, 17, PacketType.BARE);
		codec.encoders[ENCODE_FINISH_LOGIN] = new FinishLoginEncoder(0, 0, PacketType.BARE);
		return codec;
	}

	@Override
	public int getRevision() {
		return REVISION;
	}
	
}
