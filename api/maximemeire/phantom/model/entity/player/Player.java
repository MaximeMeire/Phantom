package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.model.entity.Entity;
import maximemeire.phantom.network.InboundBuffer;
import maximemeire.phantom.network.tcpip.Codec;
import maximemeire.phantom.worldserver.tcpip.codec.PacketDecoder;
import maximemeire.phantom.worldserver.tcpip.codec.PacketEncoder;
import maximemeire.phantom.worldserver.tcpip.codec.r317.IGPCodec317;
import maximemeire.phantom.worldserver.tcpip.codec.r317.PLPCodec317;
import maximemeire.phantom.worldserver.tcpip.handlers.UpstreamDispatcher;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public class Player extends Entity {
	
	private Channel channel;
	private Codec codec;
	
	@SuppressWarnings("unused")
	private int[] isaacSeeds;
	
	private static int counter = 1;
	
	public Player(Channel channel, Codec codec) {
		this.channel = channel;
		this.codec = codec;
	}

	public final Channel getChannel() {
		return channel;
	}
	
	public <T extends Player> void decodePacket(DecodePacketMessage<T> message) throws Exception {
		decodePacket(message.opcode, message.size, message.body);
	}
	
	public void decodePacket(int opcode, int size, ChannelBuffer body) throws Exception {
		InboundBuffer buffer = new InboundBuffer(body);
		PacketDecoder handler = codec.decoders[opcode];
		handler.decode(this, size, buffer);
	}
	
	public <T extends Player> void encodePacket(EncodePacketMessage<T> message) {
		encodePacket(message.opcode, message.context);
	}
	
	public void encodePacket(int opcode, Object context) {
		PacketEncoder encoder = codec.encoders[opcode];
		encoder.encode(this, context);
	}
	
	public int login(LoginDetails details) {
		int responseCode = 2;
		// TODO check credentials
		isaacSeeds = details.isaacSeeds;
		if (responseCode == 2) {
			this.codec = IGPCodec317.getCodec();
			channel.getPipeline().addAfter("PLP_UPSTREAM_DISPATCHER", "IGP_UPSTREAM_DISPATCHER", new UpstreamDispatcher(codec));
			channel.getPipeline().remove("PLP_UPSTREAM_DISPATCHER");
		}
		return 2;
	}
	
	public int logout() {
		this.codec = PLPCodec317.getCodec();
		channel.getPipeline().addAfter("IGP_UPSTREAM_DISPATCHER", "PLP_UPSTREAM_DISPATCHER", new UpstreamDispatcher(codec));
		channel.getPipeline().remove("IGP_UPSTREAM_DISPATCHER");
		return 0;
	}
	
	public int getRights() {
		return 2;
	}

	@Override
	protected int getActorIdCounter() {
		return counter++;
	}
	
	public int getCodecRevision() {
		return codec.getRevision();
	}

}
