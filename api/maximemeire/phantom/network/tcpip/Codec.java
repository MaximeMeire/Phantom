package maximemeire.phantom.network.tcpip;

import maximemeire.phantom.network.PacketDecoder;
import maximemeire.phantom.network.PacketEncoder;

public abstract class Codec {
	
	public PacketDecoder[] decoders = new PacketDecoder[256];
	public PacketEncoder[] encoders = new PacketEncoder[256];
	public abstract int getRevision();
}
