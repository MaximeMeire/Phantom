package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.model.entity.Entity;
import maximemeire.phantom.model.entity.player.update.PlayerUpdate;
import maximemeire.phantom.model.map.Location;
import maximemeire.phantom.network.ISAACCipher;
import maximemeire.phantom.network.InboundBuffer;
import maximemeire.phantom.network.PacketDecoder;
import maximemeire.phantom.network.PacketEncoder;
import maximemeire.phantom.network.tcpip.Codec;
import maximemeire.phantom.worldserver.tcpip.codec.r317.IGCodec317;
import maximemeire.phantom.worldserver.tcpip.codec.r317.PLCodec317;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public class Player extends Entity {
	
	private Channel channel;
	private Codec codec;
	
	private long serverKey;
	private LoginDetails loginDetails;
	private ISAACCipher inboundCipher;
	private ISAACCipher outboundCipher;
	
	protected PlayerUpdate update;
	
	private boolean loggedIn = false;
	private int index = 1;
	private boolean isMember = true;
	private int rights = 2;
	
	private static int counter = 1;
	
	public Player(Channel channel, Codec codec) {
		super(null);
		this.channel = channel;
		this.codec = codec;
	}
	
	public Player(Channel channel, Codec codec, Location location) {
		super(location);
		this.channel = channel;
		this.codec = codec;
	}

	public final Channel getChannel() {
		return channel;
	}
	
	public <Receiver extends Player> void decodePacket(DecodePacketMessage<Receiver> message) throws Exception {
		PacketDecoder decoder = message.decoder;
		decodePacket(decoder, message.size, message.body);
	}
	
	public void decodePacket(PacketDecoder decoder, int size, ChannelBuffer body) throws Exception {
		InboundBuffer buffer = new InboundBuffer(body);
		decoder.decode(this, size, buffer);
	}
	
	public <Receiver extends Player> void encodePacket(EncodePacketMessage<Receiver> message) {
		PacketEncoder encoder = message.encoder;
		encodePacket(encoder, message.context);
	}
	
	public void encodePacket(PacketEncoder encoder, Object context) {
		encoder.encode(this, context);
	}
	
	public void encodePacket(int opcode, Object context) {
		PacketEncoder encoder = codec.encoders[opcode];
		encoder.encode(this, context);
	}
	
	public int login(LoginDetails details) {
		int responseCode = 2;
		// TODO check credentials
		if (responseCode == 2) {
			this.codec = IGCodec317.getCodec();
			this.location = new Location(3222, 3222, 0);
			this.loginDetails = details;
			this.inboundCipher = details.getInboundCipher();
			this.outboundCipher = details.getOutboundCipher();
			this.loggedIn = true;
			// TODO load data
		}
		return 2;
	}
	
	public int logout() {
		this.codec = PLCodec317.getCodec();
		this.loggedIn = false;
		return 0;
	}
	
	public PlayerUpdate getUpdate() {
		return update;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isMember() {
		return isMember;
	}
	
	public int getRights() {
		return rights;
	}

	@Override
	protected int getActorId() {
		return counter++;
	}
	
	public Codec getCodec() {
		return codec;
	}
	
	public int getCodecRevision() {
		return codec.getRevision();
	}

	public final long getServerKey() {
		return serverKey;
	}

	public final void setServerKey(long serverKey) {
		this.serverKey = serverKey;
	}

	public void setLoginDetails(LoginDetails loginDetails) {
		this.loginDetails = loginDetails;
	}

	public LoginDetails getLoginDetails() {
		return loginDetails;
	}

	public final ISAACCipher getInboundCipher() {
		return inboundCipher;
	}

	public final ISAACCipher getOutboundCipher() {
		return outboundCipher;
	}

}
