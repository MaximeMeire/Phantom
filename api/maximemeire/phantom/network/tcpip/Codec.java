package maximemeire.phantom.network.tcpip;

import maximemeire.phantom.worldserver.tcpip.codec.PacketDecoder;
import maximemeire.phantom.worldserver.tcpip.codec.PacketEncoder;

public abstract class Codec {
	
	public PacketDecoder[] decoders = new PacketDecoder[256];
	public PacketEncoder[] encoders = new PacketEncoder[256];
	public abstract int getRevision();
}
