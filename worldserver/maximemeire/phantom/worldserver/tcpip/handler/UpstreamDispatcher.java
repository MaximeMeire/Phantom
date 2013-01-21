package maximemeire.phantom.worldserver.tcpip.handler;

import maximemeire.phantom.model.entity.player.DecodePacketMessage;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.PacketDecoder;
import maximemeire.phantom.util.Logging;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

@Sharable
public class UpstreamDispatcher extends ReplayingDecoder<DecoderState> {
	
	private static final Logger LOGGER = Logging.log();
	
	public UpstreamDispatcher() {
		super(DecoderState.READ_OPCODE);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer, DecoderState state) throws Exception {
		ChannelBuffer body = ChannelBuffers.dynamicBuffer(256);
		Player player = (Player) ctx.getChannel().getAttachment();
		if (player == null)
			throw new Exception("Attachment was NULLED!");
		int opcode = 0;
		int length = 0;
		switch (state) {
		case READ_OPCODE:
			opcode = buffer.readByte() & 0xff;
			if (player.isLoggedIn())
				opcode = opcode - player.getInboundCipher().getNextValue() & 0xff;
			checkpoint(DecoderState.READ_BODY);
		case READ_BODY:
			PacketDecoder decoder = player.getCodec().decoders[opcode];
			length = decoder.size;
			switch (length) {
			case -3:
				body.writeBytes(buffer, actualReadableBytes());
				length = body.readableBytes() - 1;
				LOGGER.info("UNKNOWN PACKET OPCODE=" + opcode + " EST.LENGTH=" + length);
				break;
			case -2:
				length = buffer.readUnsignedShort();
				body.writeBytes(buffer, length);
				LOGGER.info("PACKET OPCODE=" + opcode + " LENGTH=" + length);
				break;
			case -1:
				length = buffer.readUnsignedByte();
				body.writeBytes(buffer, length);
				LOGGER.info("PACKET OPCODE=" + opcode + " LENGTH=" + length);
				break;
				default:
					body.writeBytes(buffer, length);
					LOGGER.info("PACKET OPCODE=" + opcode + " LENGTH=" + length);
			}
			player.sendMessage(new DecodePacketMessage<Player>(decoder, length, body, player.getLink(player)));
			checkpoint(DecoderState.READ_OPCODE);
			return null;
			default:
				throw new Exception("UpstreamDispatcher failed");
		}
	}

}
