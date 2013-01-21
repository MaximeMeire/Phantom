package maximemeire.phantom.worldserver.tcpip.codec.r317;

import maximemeire.phantom.network.AnonymousPacketDecoder;
import maximemeire.phantom.network.AnonymousPacketEncoder;
import maximemeire.phantom.network.PacketEncoder.PacketType;
import maximemeire.phantom.network.tcpip.Codec;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame.MapRegionEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame.MessageEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame.PlayerDetailsEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame.ResetCameraEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.downstream.ingame.SidebarInterfaceEncoder;

public class IGCodec317 extends Codec {
	
	private static final int REVISION = 317;
	
	public static final int ENCODE_PLAYER_DETAILS = 249;
	public static final int ENCODE_RESET_CAMERA = 107;
	public static final int ENCODE_MESSAGE = 253;
	public static final int ENCODE_MAP_REGION = 73;
	public static final int ENCODE_SIDEBAR_INTERFACE = 71;
	
	public static Codec getCodec() {
		Codec codec = new PLCodec317();
		for (int i = 0; i < codec.decoders.length; i++) {
			codec.decoders[i] = new AnonymousPacketDecoder(i);
		}
		for (int i = 0; i < codec.encoders.length; i++) {
			codec.encoders[i] = new AnonymousPacketEncoder(i);
		}
		
		codec.encoders[ENCODE_PLAYER_DETAILS] = new PlayerDetailsEncoder(ENCODE_PLAYER_DETAILS, 3, PacketType.FIXED);
		codec.encoders[ENCODE_RESET_CAMERA] = new ResetCameraEncoder(ENCODE_RESET_CAMERA, 0, PacketType.FIXED);
		codec.encoders[ENCODE_MESSAGE] = new MessageEncoder(ENCODE_MESSAGE, -1, PacketType.VAR_BYTE);
		codec.encoders[ENCODE_MAP_REGION] = new MapRegionEncoder(ENCODE_MAP_REGION, 4, PacketType.FIXED);
		codec.encoders[ENCODE_SIDEBAR_INTERFACE] = new SidebarInterfaceEncoder(ENCODE_SIDEBAR_INTERFACE, 3, PacketType.FIXED);

		return codec;
	}

	@Override
	public int getRevision() {
		return REVISION;
	}

}
