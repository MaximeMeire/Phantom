package maximemeire.phantom.worldserver.tcpip.handlers;

import maximemeire.phantom.model.entity.player.DecodePacketMessage;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.network.tcpip.Codec;
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
	
	private static Codec codec;
	
	public UpstreamDispatcher(Codec codec) {
		super(DecoderState.READ_OPCODE);
		UpstreamDispatcher.codec = codec;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer, DecoderState state) throws Exception {
		LOGGER.info("Received decode request");
		ChannelBuffer body = ChannelBuffers.dynamicBuffer(256);
		int opcode = 0;
		int length = 0;
		switch (state) {
		case READ_OPCODE:
			opcode = buffer.readUnsignedByte();
			checkpoint(DecoderState.READ_BODY);
		case READ_BODY:
			length = codec.decoders[opcode].size;
			switch (length) {
			case -3:
				// Used for JS5 protocol file requests only
				length = actualReadableBytes();
				body.writeShort((short) 0); // Read in the next handler
				body.writeByte((byte) opcode); // Rewrite the opcode
				body.writeBytes(buffer, length);
				LOGGER.info("Framed JS5 request with " + (body.readableBytes() / 4) + " requests");
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
			case 0:
				body.writeBytes(buffer, actualReadableBytes());
				length = body.readableBytes() - 1;
				LOGGER.info("UNKNOWN PACKET OPCODE=" + opcode + " EST.LENGTH=" + length);
				break;
				default:
					body.writeBytes(buffer, length);
					LOGGER.info("PACKET OPCODE=" + opcode + " LENGTH=" + length);
					// Final packet length
			}
			Player player = (Player) ctx.getChannel().getAttachment();
			player.sendMessage(new DecodePacketMessage<Player>(opcode, length, body, null, player, null));
			checkpoint(DecoderState.READ_OPCODE);
			return null;
			default:
				// Should never reach here?
				throw new Exception("PLPMultiplexer failed");
		}
	}

}
